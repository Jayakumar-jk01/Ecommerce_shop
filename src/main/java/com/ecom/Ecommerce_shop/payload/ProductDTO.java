package com.ecom.Ecommerce_shop.payload;


import com.ecom.Ecommerce_shop.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotBlank
    @Size(min=3,message="product name must be atleast 3 characters")
    private String productName;

    private String description;
    private String Image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

//    private Category category;
}
