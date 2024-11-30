package com.ecom.Ecommerce_shop.service;

import com.ecom.Ecommerce_shop.payload.ProductDTO;
import com.ecom.Ecommerce_shop.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProductById(Long productId, ProductDTO productDTO);

    ProductDTO deleteProductById(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
