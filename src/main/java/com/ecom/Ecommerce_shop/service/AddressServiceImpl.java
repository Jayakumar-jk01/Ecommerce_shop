package com.ecom.Ecommerce_shop.service;


import com.ecom.Ecommerce_shop.exception.APIException;
import com.ecom.Ecommerce_shop.exception.ResourceNotFoundException;
import com.ecom.Ecommerce_shop.model.Address;
import com.ecom.Ecommerce_shop.model.User;
import com.ecom.Ecommerce_shop.payload.AddressDTO;
import com.ecom.Ecommerce_shop.repository.AddressRepository;
import com.ecom.Ecommerce_shop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address=modelMapper.map(addressDTO,Address.class);
        List<Address> addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress=addressRepository.save(address);

        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addressList=addressRepository.findAll();
        if(addressList.size()==0)
        {
            throw new APIException("No address created yet!");
        }

        List<AddressDTO> addressDTOList=addressList.stream().
                map(address -> modelMapper.map(address,AddressDTO.class)).toList();

        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException("Address","addressId",addressId));

        AddressDTO addressDTO=modelMapper.map(address,AddressDTO.class);

        return addressDTO;



    }

    @Override
    public List<AddressDTO> getAllAddressByUser(User user) {

        List<Address> addressList=user.getAddresses();

        if(addressList.size()==0)
        {
            throw  new APIException("There is no address associated with the user,Please add your address");
        }
        List<AddressDTO> addressDTOList=addressList.stream().map(address ->
                modelMapper.map(address,AddressDTO.class)).toList();

        return addressDTOList;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
       Address addressFromDB=addressRepository.findById(addressId)
               .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

       addressFromDB.setCity(addressDTO.getCity());
       addressFromDB.setStreet(addressDTO.getStreet());
       addressFromDB.setPincode(addressDTO.getPincode());
       addressFromDB.setCountry(addressDTO.getCountry());
       addressFromDB.setBuildingName(addressDTO.getBuildingName());

       Address updatedAddress=addressRepository.save(addressFromDB);

       User user=addressFromDB.getUser();

       user.getAddresses().removeIf(address->address.getAddressId().equals(addressId));
       user.getAddresses().add(updatedAddress);

       userRepository.save(user);

       return modelMapper.map(updatedAddress,AddressDTO.class);



    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address addressFromDB=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        User user=addressFromDB.getUser();

        user.getAddresses().removeIf(address->address.getAddressId().equals(addressId));


        userRepository.save(user);

        addressRepository.delete(addressFromDB);

        return "Address deleted successfully";
    }
}
