package com.ecom.Ecommerce_shop.service;


import com.ecom.Ecommerce_shop.exception.APIException;
import com.ecom.Ecommerce_shop.exception.ResourceNotFoundException;
import com.ecom.Ecommerce_shop.model.Cart;
import com.ecom.Ecommerce_shop.model.CartItem;
import com.ecom.Ecommerce_shop.model.Product;
import com.ecom.Ecommerce_shop.payload.CartDTO;
import com.ecom.Ecommerce_shop.payload.ProductDTO;
import com.ecom.Ecommerce_shop.repository.CartItemRepository;
import com.ecom.Ecommerce_shop.repository.CartRepository;
import com.ecom.Ecommerce_shop.repository.ProductRepository;
import com.ecom.Ecommerce_shop.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements  CartService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart=createCart();

        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Prouct","ProductId",productId));

        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);

        if(cartItem!=null)
        {
            throw  new APIException("Product "+product.getProductName()+" alredy exist in the cart");
        }
        if(product.getQuantity()==0)
        {
            throw new APIException("Product is out of stock");
        }

        if(product.getQuantity()<quantity)
        {
            throw  new APIException("Required quantity is not available");
        }

        CartItem newCartItem=new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity()-quantity);

        cart.setTotPrice(cart.getTotPrice()+quantity*product.getSpecialPrice());
        cartRepository.save(cart);

        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems=cart.getCartItems();

        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return  map;
        });

        cartDTO.setProductDTOS(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
       List<Cart> carts=cartRepository.findAll();
       if(carts.size()==0)
       {
           throw new APIException("No carts exist");
       }

       List<CartDTO> cartDTOS=carts.stream().map(cart -> {
           CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
           List<ProductDTO> products=  cart.getCartItems().stream().map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).collect(Collectors.toList());
           cartDTO.setProductDTOS(products);
           return cartDTO;


    }).collect(Collectors.toList());

       return cartDTOS;
    }

    @Override
    public CartDTO getCart(String email, Long cartId) {
        Cart cart=cartRepository.findCartByEmailAndCartId(email,cartId);
        if(cart==null)
        {
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }

        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);

        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDTO> products=cart.getCartItems().stream().map(p->modelMapper
                .map(p.getProduct(),ProductDTO.class)).collect(Collectors.toList());

        cartDTO.setProductDTOS(products);



        return cartDTO;

    }


    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long prductId, Integer quantity) {
        String email=authUtil.loggedInEmail();
        Cart userCart=cartRepository.findCartByEmail(email);
        Long cartId=userCart.getCartId();

        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart","cartid",cartId));

        Product product=productRepository.findById(prductId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",prductId));

        if(product.getQuantity()==0)
        {
            throw new APIException("Product is not available");
        }
        if(product.getQuantity()<quantity)
        {
            throw new APIException("Required quantity is higher than available product quantity for this product "+product.getProductName());
        }
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,prductId);

        if(cartItem==null)
        {
            throw new APIException("Product is not available");
        }
        int newQuantity=cartItem.getQuantity()+quantity;
        if(newQuantity<0)
        {
            throw new APIException("Quanityt cannot be less than zero");
        }
        if(newQuantity==0)
        {
            deletProductFromCart(cartId,prductId);
        }
        else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());

            cart.setTotPrice(cart.getTotPrice() + (cartItem.getProductPrice() * quantity));

            cartRepository.save(cart);
        }
        CartItem updatedCartItem=cartItemRepository.save(cartItem);
        if(updatedCartItem.getQuantity()==0)
        {
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }

        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems=cart.getCartItems();

        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO prd=modelMapper.map(
                    item.getProduct(),ProductDTO.class
            );
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProductDTOS(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    @Transactional
    public String deletProductFromCart(Long cartId, Long productId) {
       Cart cart=cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("Cart","cartId",cartId));

       CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
       if (cartItem==null)
       {
           throw new ResourceNotFoundException("Product","productId",productId);
       }

       cart.setTotPrice(cart.getTotPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));

       cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);

       return "Product removed from the cart successfully";

    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart","cartid",cartId));

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);

        if (cartItem==null)
        {
            throw new ResourceNotFoundException("Product","productId",productId);
        }

        double cartPrice=cart.getTotPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotPrice(cartPrice + (cartItem.getProductPrice()*cartItem.getQuantity()));

        cartItemRepository.save(cartItem);

    }


    public Cart createCart()
    {
        Cart usercart=cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(usercart!=null)
            return usercart;

        Cart cart=new Cart();
        cart.setTotPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart=cartRepository.save(cart);

        return newCart;


    }
}
