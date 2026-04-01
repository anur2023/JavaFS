package com.example.FashionE_CommercewithVirtualTry_On.module.auth.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.request.LoginRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.request.RegisterRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.response.JwtResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 🔹 Register API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // 🔹 Login API
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
        String response = authService.registerAdmin(request);
        return ResponseEntity.ok(response);
    }
}