package com.ecom.Ecommerce_shop.controller;


import com.ecom.Ecommerce_shop.model.User;
import com.ecom.Ecommerce_shop.payload.AddressDTO;
import com.ecom.Ecommerce_shop.service.AddressService;
import com.ecom.Ecommerce_shop.util.AuthUtil;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO)
    {
        User user=authUtil.loggedInUser();

        AddressDTO savedAddressDTO=addressService.createAddress(addressDTO,user);


        return  new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses()
    {

        List<AddressDTO> addressDTOList=addressService.getAllAddresses();


        return  new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId)
    {

      AddressDTO addressDTO=addressService.getAddressById(addressId);


        return  new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressById()
    {

        User user=authUtil.loggedInUser();


        List<AddressDTO> addressDTOList=addressService.getAllAddressByUser(user);


        return  new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO )
    {
        AddressDTO updatedAddressDTO=addressService.updateAddressById(addressId,addressDTO);

        return  new ResponseEntity<>(updatedAddressDTO,HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId )
    {
       String status=addressService.deleteAddressById(addressId);

       return new ResponseEntity<>(status,HttpStatus.OK);
    }



}
