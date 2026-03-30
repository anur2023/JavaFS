package com.std.ecommerce.module.product.repository;

import com.std.ecommerce.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByVendorId(Long vendorId);

    List<Product> findByCategoryId(Long categoryId);


    List<Product> findByNameContainingIgnoreCase(String searchTerm);


    @Query("SELECT p FROM Product p WHERE p.vendor.id = :vendorId AND p.category.id = :categoryId")
    List<Product> findByVendorIdAndCategoryId(@Param("vendorId") Long vendorId,
                                              @Param("categoryId") Long categoryId);


    boolean existsByNameAndVendorId(String name, Long vendorId);
}