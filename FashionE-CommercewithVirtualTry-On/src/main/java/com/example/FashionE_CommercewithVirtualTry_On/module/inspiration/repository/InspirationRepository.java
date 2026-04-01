package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.entity.Inspiration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspirationRepository extends JpaRepository<Inspiration, Long> {

    // 🔹 Get latest posts (newest first)
    List<Inspiration> findAllByOrderByCreatedAtDesc();

    // 🔹 Search by title
    List<Inspiration> findByTitleContainingIgnoreCase(String title);
}