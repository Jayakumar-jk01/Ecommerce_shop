package com.ecom.Ecommerce_shop.repository;

import com.ecom.Ecommerce_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);
}
