package com.std.ecommerce.module.cart.repository;

import com.std.ecommerce.module.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {

    // 🔹 Get all cart items for a user
    List<Cart> findByUserId(Long userId);

    // 🔹 Find specific cart item by user and product
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    // 🔹 Delete all cart items for a user (clear cart)
    void deleteByUserId(Long userId);
}