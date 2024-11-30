package com.ecom.Ecommerce_shop.repository;

import com.ecom.Ecommerce_shop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Locale;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);
}
