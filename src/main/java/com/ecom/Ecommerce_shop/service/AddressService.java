package com.ecom.Ecommerce_shop.service;

import com.ecom.Ecommerce_shop.model.User;
import com.ecom.Ecommerce_shop.payload.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);


    List<AddressDTO> getAllAddressByUser(User user);

    AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO);

    String deleteAddressById(Long addressId);
}
