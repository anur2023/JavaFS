package com.std.ecommerce.module.vendor.controller;

import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.vendor.dto.VendorRequest;
import com.std.ecommerce.module.vendor.dto.VendorResponse;
import com.std.ecommerce.module.vendor.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // POST /api/vendors/register
    // Logged-in VENDOR user creates their own store profile
    @PostMapping("/register")
    public ResponseEntity<VendorResponse> registerVendor(
            @AuthenticationPrincipal User currentUser,
            @RequestBody VendorRequest request) {
        return ResponseEntity.ok(vendorService.createVendorProfile(currentUser.getId(), request));
    }

    // GET /api/vendors/me
    // Logged-in VENDOR sees their own profile
    @GetMapping("/me")
    public ResponseEntity<VendorResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(vendorService.getMyProfile(currentUser.getId()));
    }

    // PUT /api/vendors/me
    // Logged-in VENDOR updates their own profile
    @PutMapping("/me")
    public ResponseEntity<VendorResponse> updateMyProfile(
            @AuthenticationPrincipal User currentUser,
            @RequestBody VendorRequest request) {
        return ResponseEntity.ok(vendorService.updateVendorProfile(currentUser.getId(), request));
    }

    // GET /api/vendors
    // Public: list all vendor stores
    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    // GET /api/vendors/{id}
    // Public: get a specific vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getVendorById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    // DELETE /api/vendors/admin/{id}
    // Admin deletes a vendor profile
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.deleteVendor(id));
    }
}