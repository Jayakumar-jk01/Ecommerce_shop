package com.ecom.Ecommerce_shop.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartItemDTO cartItemDTO;
    private ProductDTO productDTO;
    private  Integer quantity;
    private double distcount;
    private double productPrice;
}
