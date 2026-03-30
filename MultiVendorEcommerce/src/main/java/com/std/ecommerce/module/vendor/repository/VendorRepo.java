package com.std.ecommerce.module.vendor.repository;

import com.std.ecommerce.module.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepo extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByStoreName(String storeName);
}
