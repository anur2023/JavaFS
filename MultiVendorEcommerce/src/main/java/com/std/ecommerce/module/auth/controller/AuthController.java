package com.std.ecommerce.module.auth.controller;

import com.std.ecommerce.module.auth.dto.RegisterRequest;
import com.std.ecommerce.module.auth.dto.RegisterResponse;
import com.std.ecommerce.module.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 🔥 REGISTER API
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}