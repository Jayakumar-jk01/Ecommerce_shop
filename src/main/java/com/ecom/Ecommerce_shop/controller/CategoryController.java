package com.ecom.Ecommerce_shop.controller;


import com.ecom.Ecommerce_shop.config.AppConstants;
import com.ecom.Ecommerce_shop.model.Category;
import com.ecom.Ecommerce_shop.payload.CategoryDTO;
import com.ecom.Ecommerce_shop.payload.CategoryResponse;
import com.ecom.Ecommerce_shop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public  CategoryController(CategoryService categoryService)
    {
        this.categoryService=categoryService;
    }






    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.page_Number,required = false)Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.page_Size,required = false)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.sort_Category_By,required = false)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.sort_Dir,required = false)String sortOrder)
    {
        //return categoryService.getAllCategories()
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);

    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO)
    {
       CategoryDTO savedCategoryDTO= categoryService.addCategory(categoryDTO);


        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable Long categoryId) {

           CategoryDTO deleteCategory = categoryService.deleteCategory(categoryId);

            return new ResponseEntity<>(deleteCategory, HttpStatus.OK);


    }

    @PutMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> update(@Valid @RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId)
    {


            CategoryDTO updatedCategoryDTO=categoryService.updateCategory(categoryDTO,categoryId);

            return new ResponseEntity<>(updatedCategoryDTO,HttpStatus.OK);


    }

}
