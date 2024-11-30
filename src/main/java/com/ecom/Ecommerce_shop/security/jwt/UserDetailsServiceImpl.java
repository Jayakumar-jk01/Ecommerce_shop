package com.ecom.Ecommerce_shop.security.jwt;

import com.ecom.Ecommerce_shop.model.User;
import com.ecom.Ecommerce_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    UserDetailsImpl userDetailsImpl;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User Not found"));

        return UserDetailsImpl.build(user);

    }
}
