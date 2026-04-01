package com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUserUserId(Long userId);
}