package com.ecom.Ecommerce_shop.service;

import com.ecom.Ecommerce_shop.payload.CartDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);


    List<CartDTO> getAllCarts();

    CartDTO getCart(String email, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long prductId, Integer quantity);

    @Transactional
    String deletProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

//    void updateProductInCarts(Long cartId, Long productId);
}
