package com.ecom.Ecommerce_shop.controller;

import com.ecom.Ecommerce_shop.model.AppRole;
import com.ecom.Ecommerce_shop.model.Role;
import com.ecom.Ecommerce_shop.model.User;
import com.ecom.Ecommerce_shop.repository.RoleRepository;
import com.ecom.Ecommerce_shop.repository.UserRepository;
import com.ecom.Ecommerce_shop.security.jwt.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication;

        try{
            authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

        }
        catch(AuthenticationException exception){
            Map<String,Object> map=new HashMap<>();
            map.put("message","Bad Credentials");
            map.put("status",false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie=jwtUtils.generateJwtCookie(userDetails);

        List<String> roles=userDetails.getAuthorities().stream().map(item->item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse loginResponse=new UserInfoResponse(userDetails.getId(),jwtCookie.toString(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if(userRepository.existsByUserName(signUpRequest.getUsername()))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error:Username already exists"));
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail()))
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error:Email already exists"));
        }

        User user =new User(signUpRequest.getUsername(), signUpRequest.getEmail(),encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles=signUpRequest.getRole();
        Set<Role> roles=new HashSet<>();

        if(strRoles==null)
        {
            Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER).
                    orElseThrow(()->new RuntimeException("Error:Role Not found"));

            roles.add(userRole);
        }else{
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole=roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()->new RuntimeException("Role Not found"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole=roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(()->new RuntimeException("Role Not found"));
                        roles.add(sellerRole);
                        break;

                    default:

                        Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()->new RuntimeException("Role Not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User created Successfully"));
    }

    @GetMapping("/username")
    public String getUserName(Authentication authentication)
    {
        if(authentication!=null)
            return authentication.getName();
        else {
            return "NULL";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication)
    {
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities().stream().map(item->item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse loginResponse=new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout()
    {

        ResponseCookie cookie=jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new MessageResponse("You are logged out"));


    }




}
