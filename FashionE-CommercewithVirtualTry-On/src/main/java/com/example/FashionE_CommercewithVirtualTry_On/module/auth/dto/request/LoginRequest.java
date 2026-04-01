package com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.request;

public class LoginRequest {

    private String email;
    private String password;

    // 🔹 Default Constructor
    public LoginRequest() {
    }

    // 🔹 Parameterized Constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 🔹 Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}