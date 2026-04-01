package com.example.FashionE_CommercewithVirtualTry_On.module.product.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 🔹 Search by name (basic search)
    List<Product> findByNameContainingIgnoreCase(String name);

    // 🔹 Filter by category
    List<Product> findByCategory(String category);

    // 🔹 Filter by brand
    List<Product> findByBrand(String brand);

    // 🔹 Filter by price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
}