package com.example.FashionE_CommercewithVirtualTry_On.module.product.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Add product-specific queries here as needed
}