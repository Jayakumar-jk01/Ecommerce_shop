package com.ecom.Ecommerce_shop.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min =3,message = "Street name should be atleast 3 characters")
    private String street;

    @NotBlank
    @Size(min =3,message = "Building name should be atleast 3 characters")
    private String buildingName;

    @NotBlank
    @Size(min =3,message = "city name should be atleast 3 characters")
    private String city;

    @NotBlank
    @Size(min =3,message = "Country name should be atleast 3 characters")
    private String country;

    @NotBlank
    @Size(min =6,message = "pincode should be atleast 6 characters")
    private String pincode;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String buildingName, String city, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
    }
}
