package com.std.ecommerce.module.vendor.service;

import com.std.ecommerce.module.auth.entity.Role;
import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.auth.repository.UserRepository;
import com.std.ecommerce.module.vendor.dto.VendorRequest;
import com.std.ecommerce.module.vendor.dto.VendorResponse;
import com.std.ecommerce.module.vendor.entity.Vendor;
import com.std.ecommerce.module.vendor.repository.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private UserRepository userRepository;

    // CREATE vendor profile (called by the logged-in VENDOR user)
    public VendorResponse createVendorProfile(Long userId, VendorRequest request) {

        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Only VENDOR role can create a vendor profile
        if (!user.getRole().equals(Role.VENDOR)) {
            throw new RuntimeException("Only users with VENDOR role can create a vendor profile");
        }

        // Prevent duplicate vendor profile
        if (vendorRepo.existsByUserId(userId)) {
            throw new RuntimeException("Vendor profile already exists for this user");
        }

        // Prevent duplicate store names
        if (vendorRepo.existsByStoreName(request.getStoreName())) {
            throw new RuntimeException("Store name already taken: " + request.getStoreName());
        }

        Vendor vendor = new Vendor();
        vendor.setUser(user);
        vendor.setStoreName(request.getStoreName());
        vendor.setDescription(request.getDescription());

        Vendor saved = vendorRepo.save(vendor);
        return toResponse(saved);
    }

    // GET my vendor profile (Vendor sees their own)
    public VendorResponse getMyProfile(Long userId) {
        Vendor vendor = vendorRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No vendor profile found for this user"));
        return toResponse(vendor);
    }

    // UPDATE vendor profile
    public VendorResponse updateVendorProfile(Long userId, VendorRequest request) {
        Vendor vendor = vendorRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No vendor profile found for this user"));

        // Check store name conflict only if name is changing
        if (!vendor.getStoreName().equals(request.getStoreName()) &&
                vendorRepo.existsByStoreName(request.getStoreName())) {
            throw new RuntimeException("Store name already taken: " + request.getStoreName());
        }

        vendor.setStoreName(request.getStoreName());
        vendor.setDescription(request.getDescription());

        Vendor updated = vendorRepo.save(vendor);
        return toResponse(updated);
    }

    // GET ALL vendors (Admin or public listing)
    public List<VendorResponse> getAllVendors() {
        return vendorRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET vendor by ID (Public)
    public VendorResponse getVendorById(Long vendorId) {
        Vendor vendor = vendorRepo.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
        return toResponse(vendor);
    }

    // DELETE vendor profile (Admin only)
    public String deleteVendor(Long vendorId) {
        Vendor vendor = vendorRepo.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
        vendorRepo.delete(vendor);
        return "Vendor profile deleted successfully";
    }

    // Helper: Entity → Response DTO
    private VendorResponse toResponse(Vendor vendor) {
        return new VendorResponse(
                vendor.getId(),
                vendor.getUser().getId(),
                vendor.getUser().getName(),
                vendor.getUser().getEmail(),
                vendor.getStoreName(),
                vendor.getDescription()
        );
    }
}