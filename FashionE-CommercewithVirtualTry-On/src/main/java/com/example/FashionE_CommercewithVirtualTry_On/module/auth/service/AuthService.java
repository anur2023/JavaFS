package com.example.FashionE_CommercewithVirtualTry_On.module.auth.service;

import com.example.FashionE_CommercewithVirtualTry_On.common.security.JwtTokenProvider;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.request.LoginRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.request.RegisterRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.dto.response.JwtResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.Role;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔹 Register
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐 ENCRYPTED
        user.setFullName(request.getFullName());
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully";
    }

    // 🔹 Login
    public JwtResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new JwtResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

    public String registerAdmin(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.ADMIN); // 🔥 ADMIN ROLE
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "Admin registered successfully";
    }
}