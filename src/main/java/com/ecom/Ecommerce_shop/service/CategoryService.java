package com.ecom.Ecommerce_shop.service;

import com.ecom.Ecommerce_shop.model.Category;
import com.ecom.Ecommerce_shop.payload.CategoryDTO;
import com.ecom.Ecommerce_shop.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);
}
