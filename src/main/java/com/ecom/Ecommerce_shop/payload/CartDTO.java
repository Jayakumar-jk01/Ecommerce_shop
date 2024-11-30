package com.ecom.Ecommerce_shop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long cartId;
    private  double totalprice=0.0;
    private List<ProductDTO> productDTOS =new ArrayList<>();
}
