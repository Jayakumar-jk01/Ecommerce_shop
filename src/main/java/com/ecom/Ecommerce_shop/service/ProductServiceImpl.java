package com.ecom.Ecommerce_shop.service;


import com.ecom.Ecommerce_shop.exception.APIException;
import com.ecom.Ecommerce_shop.exception.ResourceNotFoundException;
import com.ecom.Ecommerce_shop.model.Cart;
import com.ecom.Ecommerce_shop.model.Category;
import com.ecom.Ecommerce_shop.model.Product;
import com.ecom.Ecommerce_shop.payload.CartDTO;
import com.ecom.Ecommerce_shop.payload.ProductDTO;
import com.ecom.Ecommerce_shop.payload.ProductResponse;
import com.ecom.Ecommerce_shop.repository.CartRepository;
import com.ecom.Ecommerce_shop.repository.CategoryRepository;
import com.ecom.Ecommerce_shop.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;


    @Value("${product.image}")
    private String path;



    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category=categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryid",categoryId)
        );

        boolean isProductNotPresent=true;
        List<Product> products=category.getProducts();

        for(int i=0;i<products.size();i++)
        {
            if(products.get(i).getProductName().equals(productDTO.getProductName()))
            {
                isProductNotPresent=false;
                break;
            }
        }

        if(isProductNotPresent) {

            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);

            product.setProductImage("default.png");

            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepository.save(product);


            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else{
            throw  new APIException("Product Already Exist!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=productRepository.findAll(pageDetails);



        List<Product> products=pageProducts.getContent();

        List<ProductDTO> productDTOS=products.stream().map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryid",categoryId)
        );
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=  productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);


        List<Product> products=pageProducts.getContent();

        List<ProductDTO> productDTOS=products.stream().map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;

    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts=  productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);


        List<Product> products=pageProducts.getContent();
        if(products.isEmpty())
        {
            throw  new APIException("There are no products with this keyword!!");
        }

        List<ProductDTO> productDTOS=products.stream().map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProductById(Long productId, ProductDTO productDTO) {
       Product productFromDB=productRepository.findById(productId)
               .orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));



       Product product=modelMapper.map(productDTO,Product.class);

       productFromDB.setProductName(product.getProductName());
       productFromDB.setDescription(product.getDescription());
       productFromDB.setQuantity(product.getQuantity());
       productFromDB.setDiscount(product.getDiscount());
       productFromDB.setPrice(product.getPrice());
       productFromDB.setSpecialPrice(product.getSpecialPrice());

       Product savedProduct=productRepository.save(productFromDB);

       List<Cart> carts=cartRepository.findCartsByProductId(productId);

       List<CartDTO> cartDTOS=carts.stream().map(cart -> {
           CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
           List<ProductDTO> products=cart.getCartItems().stream().map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
                   .collect(Collectors.toList());
           cartDTO.setProductDTOS(products);
           return cartDTO;
       }).collect(Collectors.toList());
       cartDTOS.forEach(cart->cartService.updateProductInCarts(cart.getCartId(),productId));

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProductById(Long productId) {

        Product savedProduct=productRepository.findById(productId).
                orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));


        List<Cart> carts=cartRepository.findCartsByProductId(productId);

        carts.forEach(cart -> cartService.deletProductFromCart(cart.getCartId(),productId));

        productRepository.deleteById(productId);


        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        Product productFromDB=productRepository.findById(productId).
                orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        //images folder at root directory

        String fileName=fileService.uploadImage(path,image);

        productFromDB.setProductImage(fileName);
        Product savedProduct=productRepository.save(productFromDB);

        return modelMapper.map(savedProduct,ProductDTO.class);





    }


}
