package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request.AddLookbookItemRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request.CreateLookbookRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request.UpdateLookbookRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.response.LookbookResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.service.LookbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin-facing Lookbook endpoints.
 * Base URL : /api/admin/lookbooks
 * Secured  : ROLE_ADMIN  (via SecurityConfig — /api/admin/** requires ADMIN)
 */
@RestController
@RequestMapping("/api/admin/lookbooks")
public class AdminLookbookController {

    @Autowired
    private LookbookService lookbookService;

    /**
     * POST /api/admin/lookbooks
     * Create a new lookbook (starts as DRAFT).
     * Body: { "title", "description", "season", "style", "coverImageUrl", "productIds": [] }
     */
    @PostMapping
    public ResponseEntity<LookbookResponse> createLookbook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateLookbookRequest request) {

        return ResponseEntity.ok(
                lookbookService.createLookbook(userDetails.getUsername(), request));
    }

    /**
     * PUT /api/admin/lookbooks/{lookbookId}
     * Update metadata or status of a lookbook.
     * Body: { "title", "description", "season", "style", "coverImageUrl", "status" }
     */
    @PutMapping("/{lookbookId}")
    public ResponseEntity<LookbookResponse> updateLookbook(
            @PathVariable Long lookbookId,
            @RequestBody UpdateLookbookRequest request) {

        return ResponseEntity.ok(lookbookService.updateLookbook(lookbookId, request));
    }

    /**
     * DELETE /api/admin/lookbooks/{lookbookId}
     * Permanently delete a lookbook and all its items.
     */
    @DeleteMapping("/{lookbookId}")
    public ResponseEntity<String> deleteLookbook(@PathVariable Long lookbookId) {
        return ResponseEntity.ok(lookbookService.deleteLookbook(lookbookId));
    }

    /**
     * GET /api/admin/lookbooks
     * Get all lookbooks (DRAFT + PUBLISHED + ARCHIVED).
     */
    @GetMapping
    public ResponseEntity<List<LookbookResponse>> getAllLookbooks() {
        return ResponseEntity.ok(lookbookService.getAllLookbooksAdmin());
    }

    /**
     * POST /api/admin/lookbooks/{lookbookId}/items
     * Add a product to a lookbook.
     * Body: { "productId", "stylingNote", "displayOrder" }
     */
    @PostMapping("/{lookbookId}/items")
    public ResponseEntity<LookbookResponse> addItem(
            @PathVariable Long lookbookId,
            @RequestBody AddLookbookItemRequest request) {

        return ResponseEntity.ok(lookbookService.addItemToLookbook(lookbookId, request));
    }

    /**
     * DELETE /api/admin/lookbooks/{lookbookId}/items/{productId}
     * Remove a product from a lookbook.
     */
    @DeleteMapping("/{lookbookId}/items/{productId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long lookbookId,
            @PathVariable Long productId) {

        return ResponseEntity.ok(lookbookService.removeItemFromLookbook(lookbookId, productId));
    }

    /**
     * PATCH /api/admin/lookbooks/{lookbookId}/publish
     * Convenience endpoint to publish a lookbook in one call.
     */
    @PatchMapping("/{lookbookId}/publish")
    public ResponseEntity<LookbookResponse> publishLookbook(@PathVariable Long lookbookId) {
        UpdateLookbookRequest req = new UpdateLookbookRequest();
        req.setStatus("PUBLISHED");
        return ResponseEntity.ok(lookbookService.updateLookbook(lookbookId, req));
    }

    /**
     * PATCH /api/admin/lookbooks/{lookbookId}/archive
     * Convenience endpoint to archive a lookbook.
     */
    @PatchMapping("/{lookbookId}/archive")
    public ResponseEntity<LookbookResponse> archiveLookbook(@PathVariable Long lookbookId) {
        UpdateLookbookRequest req = new UpdateLookbookRequest();
        req.setStatus("ARCHIVED");
        return ResponseEntity.ok(lookbookService.updateLookbook(lookbookId, req));
    }
}