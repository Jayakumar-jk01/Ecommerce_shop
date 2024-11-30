package com.ecom.Ecommerce_shop.security.jwt;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 3,max = 30)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min=6,max=40)
    private String password;

    public @NotBlank @Size(min = 3, max = 30) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 3, max = 30) String username) {
        this.username = username;
    }

    public @NotBlank @Size(max = 60) @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Size(max = 60) @Email String email) {
        this.email = email;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public @NotBlank @Size(min = 6, max = 40) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 6, max = 40) String password) {
        this.password = password;
    }
}
