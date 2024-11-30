package com.ecom.Ecommerce_shop.payload;

import com.ecom.Ecommerce_shop.model.User;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {


    private Long addressId;


    private String street;


    private String buildingName;


    private String city;


    private String country;


    private String pincode;


//    private List<User> users=new ArrayList<>();
}
