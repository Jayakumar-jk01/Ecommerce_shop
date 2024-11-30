package com.ecom.Ecommerce_shop.repository;

import com.ecom.Ecommerce_shop.model.AppRole;
import com.ecom.Ecommerce_shop.model.Role;
import com.ecom.Ecommerce_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(AppRole appRole);
}
