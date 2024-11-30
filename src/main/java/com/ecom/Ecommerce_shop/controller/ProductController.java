package com.ecom.Ecommerce_shop.controller;


import com.ecom.Ecommerce_shop.config.AppConstants;
import com.ecom.Ecommerce_shop.model.Product;
import com.ecom.Ecommerce_shop.payload.ProductDTO;
import com.ecom.Ecommerce_shop.payload.ProductResponse;
import com.ecom.Ecommerce_shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {


    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid  @RequestBody ProductDTO productDTO, @PathVariable Long categoryId)
    {

        ProductDTO savedProductDTO=productService.addProduct(categoryId,productDTO);


        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse>
    getAllProducts( @RequestParam(name = "pageNumber",defaultValue = AppConstants.page_Number,required = false)Integer pageNumber,
                    @RequestParam(name = "pageSize",defaultValue = AppConstants.page_Size,required = false)Integer pageSize,
                    @RequestParam(name = "sortBy",defaultValue = AppConstants.sort_Product_By,required = false)String sortBy,
                    @RequestParam(name = "sortOrder",defaultValue = AppConstants.sort_Dir,required = false)String sortOrder)
    {
        ProductResponse productResponse=productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId,
                                                                    @RequestParam(name = "pageNumber",defaultValue = AppConstants.page_Number,required = false)Integer pageNumber,
                                                                    @RequestParam(name = "pageSize",defaultValue = AppConstants.page_Size,required = false)Integer pageSize,
                                                                    @RequestParam(name = "sortBy",defaultValue = AppConstants.sort_Product_By,required = false)String sortBy,
                                                                    @RequestParam(name = "sortOrder",defaultValue = AppConstants.sort_Dir,required = false)String sortOrder)
    {
        ProductResponse productResponse=productService.getAllProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);

        return  new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsBykeyword(@PathVariable String keyword, @RequestParam(name = "pageNumber",defaultValue = AppConstants.page_Number,required = false)Integer pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue = AppConstants.page_Size,required = false)Integer pageSize,
                                                                @RequestParam(name = "sortBy",defaultValue = AppConstants.sort_Product_By,required = false)String sortBy,
                                                                @RequestParam(name = "sortOrder",defaultValue = AppConstants.sort_Dir,required = false)String sortOrder)
    {
        ProductResponse productResponse=productService.searchProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);

    }


    @PutMapping("/admin/products/{productId}")
    public  ResponseEntity<ProductDTO> updateProductById(@PathVariable Long productId,@Valid @RequestBody ProductDTO productDTO)
    {

        ProductDTO updatedProductDto=productService.updateProductById(productId,productDTO);

        return new ResponseEntity<>(updatedProductDto,HttpStatus.OK);

    }


    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable Long productId)
    {
        ProductDTO productDTO=productService.deleteProductById(productId);

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateImage(@PathVariable Long productId
            , @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO productDTO=productService.updateProductImage(productId,image);

        return  new ResponseEntity<>(productDTO,HttpStatus.OK);
    }






}
