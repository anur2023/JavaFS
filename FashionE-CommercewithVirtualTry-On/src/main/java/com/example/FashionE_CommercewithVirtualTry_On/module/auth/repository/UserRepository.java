package com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Find user by email (used in login)
    Optional<User> findByEmail(String email);

    // 🔹 Check if email already exists (used in registration)
    boolean existsByEmail(String email);
}