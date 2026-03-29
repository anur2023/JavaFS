package com.std.ecommerce.module.auth.dto;

import com.std.ecommerce.module.auth.entity.Role;

public class RegisterResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;

    // 🔹 Default Constructor
    public RegisterResponse() {
    }

    // 🔹 Parameterized Constructor
    public RegisterResponse(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // 🔹 Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}