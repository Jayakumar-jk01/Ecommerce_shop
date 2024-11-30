package com.ecom.Ecommerce_shop.controller;


import com.ecom.Ecommerce_shop.model.Cart;
import com.ecom.Ecommerce_shop.payload.CartDTO;
import com.ecom.Ecommerce_shop.repository.CartRepository;
import com.ecom.Ecommerce_shop.service.CartService;
import com.ecom.Ecommerce_shop.util.AuthUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private AuthUtil authutil;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;



    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,@PathVariable Integer quantity)
    {
        CartDTO cartDTO=cartService.addProductToCart(productId,quantity);

        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO> >getAllCarts(){
        List<CartDTO> cartDTOS=cartService.getAllCarts();

        return new ResponseEntity<>(cartDTOS,HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCratById()
    {
        String email=authutil.loggedInEmail();
        Cart cart=cartRepository.findCartByEmail(email);
        Long cartId= cart.getCartId();
        CartDTO cartDTO=cartService.getCart(email,cartId);

        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,@PathVariable String operation)
    {
        CartDTO cartDTO=cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete")?-1:1);

        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,@PathVariable Long productId)
    {
        String status=cartService.deletProductFromCart(cartId,productId);

        return new ResponseEntity<>(status,HttpStatus.OK);
    }


}
