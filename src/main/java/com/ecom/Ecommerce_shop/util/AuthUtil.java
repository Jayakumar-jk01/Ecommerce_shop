package com.ecom.Ecommerce_shop.util;

import com.ecom.Ecommerce_shop.model.User;
import com.ecom.Ecommerce_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("Username not found"));

        return user.getEmail();
    }

    public Long loggedInUserId()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("Username not found"));

        return user.getUserId();
    }

    public User loggedInUser()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("Username not found"));

        return user;
    }
}
