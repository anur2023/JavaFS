package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.dto.response.TryOnResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.service.TryOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin-facing Virtual Try-On endpoints.
 * Base URL : /api/admin/tryon
 * Secured  : ROLE_ADMIN  (via SecurityConfig — /api/admin/** requires ADMIN role)
 */
@RestController
@RequestMapping("/api/admin/tryon")
public class AdminTryOnController {

    @Autowired
    private TryOnService tryOnService;

    /**
     * GET /api/admin/tryon
     * Get all try-on sessions across all users.
     */
    @GetMapping
    public ResponseEntity<List<TryOnResponse>> getAllSessions() {
        return ResponseEntity.ok(tryOnService.getAllSessions());
    }

    /**
     * GET /api/admin/tryon/product/{productId}
     * Get all try-on sessions for a specific product.
     * Useful for understanding which products are tried on most.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TryOnResponse>> getSessionsByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(tryOnService.getSessionsByProduct(productId));
    }
}