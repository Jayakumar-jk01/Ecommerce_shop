package com.ecom.Ecommerce_shop.service;


import com.ecom.Ecommerce_shop.exception.APIException;
import com.ecom.Ecommerce_shop.exception.ResourceNotFoundException;
import com.ecom.Ecommerce_shop.model.Category;
import com.ecom.Ecommerce_shop.payload.CategoryDTO;
import com.ecom.Ecommerce_shop.payload.CategoryResponse;
import com.ecom.Ecommerce_shop.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategorySeviceImpl implements  CategoryService{

//    private List<Category> categories=new ArrayList<>();
    private Long nextid=1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> pageCategoires=categoryRepository.findAll(pageDetails);
        List<Category> categories=pageCategoires.getContent();

//        List<Category> categories=categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No categories created till now");

        List<CategoryDTO> categoryDTOS=categories.stream().
                map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(pageCategoires.getNumber());
        categoryResponse.setPageSize(pageCategoires.getSize());
        categoryResponse.setTotalElements( pageCategoires.getTotalElements());
        categoryResponse.setTotalPages(pageCategoires.getTotalPages());
        categoryResponse.setLastPage(pageCategoires.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        Category category=modelMapper.map(categoryDTO,Category.class);

        Category savedCategoryDB=categoryRepository.findByCategoryName(category.getCategoryName());

        if(savedCategoryDB!=null)
            throw new APIException("Category Already exist");

//        category.setCategoryId(nextid++);

        Category savedCategory=categoryRepository.save(category);

        CategoryDTO savedCategoryDTO=modelMapper.map(savedCategory, CategoryDTO.class);

        return savedCategoryDTO;

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        Optional<Category> savedCategoryOptional=categoryRepository.findById(categoryId);

        Category savedCategory=savedCategoryOptional.orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryId",categoryId));




       categoryRepository.delete(savedCategory);
       CategoryDTO savedCategoryDTO=modelMapper.map(savedCategory,CategoryDTO.class);

       return savedCategoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        Category category=modelMapper.map(categoryDTO,Category.class);

        Category existingCategory=categoryRepository.findByCategoryName(category.getCategoryName());

        if(existingCategory!=null)
            throw new APIException("Category Already exist");


       Optional<Category> savedOptionalCategory=categoryRepository.findById(categoryId);

       Category savedCategory=savedOptionalCategory.orElseThrow(
               ()->new ResourceNotFoundException("Category","categoryId",categoryId));




       category.setCategoryId(categoryId);
       savedCategory=categoryRepository.save(category);
       CategoryDTO savedCategoryDTO=modelMapper.map(savedCategory,CategoryDTO.class);
       return  savedCategoryDTO;


    }
}
