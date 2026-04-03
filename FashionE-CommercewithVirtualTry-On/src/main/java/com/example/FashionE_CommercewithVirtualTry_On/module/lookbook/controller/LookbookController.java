package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.response.LookbookResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.service.LookbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Customer-facing Lookbook endpoints.
 * Base URL : /api/user/lookbooks
 * Secured  : ROLE_CUSTOMER  (via SecurityConfig — /api/user/** requires CUSTOMER)
 */
@RestController
@RequestMapping("/api/user/lookbooks")
public class LookbookController {

    @Autowired
    private LookbookService lookbookService;

    /**
     * GET /api/user/lookbooks
     * Browse all PUBLISHED lookbooks.
     * Optional query params:
     *   ?season=Summer 2025
     *   ?style=Casual
     *   ?search=denim
     */
    @GetMapping
    public ResponseEntity<List<LookbookResponse>> getPublishedLookbooks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(
                lookbookService.getPublishedLookbooks(
                        userDetails.getUsername(), season, style, search));
    }

    /**
     * GET /api/user/lookbooks/{lookbookId}
     * Get details of a single PUBLISHED lookbook.
     */
    @GetMapping("/{lookbookId}")
    public ResponseEntity<LookbookResponse> getLookbookById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long lookbookId) {

        return ResponseEntity.ok(
                lookbookService.getPublishedLookbookById(
                        userDetails.getUsername(), lookbookId));
    }

    /**
     * POST /api/user/lookbooks/{lookbookId}/save
     * Bookmark / save a lookbook to the user's collection.
     */
    @PostMapping("/{lookbookId}/save")
    public ResponseEntity<String> saveLookbook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long lookbookId) {

        return ResponseEntity.ok(
                lookbookService.saveLookbook(userDetails.getUsername(), lookbookId));
    }

    /**
     * DELETE /api/user/lookbooks/{lookbookId}/save
     * Remove a lookbook from the user's saved collection.
     */
    @DeleteMapping("/{lookbookId}/save")
    public ResponseEntity<String> unsaveLookbook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long lookbookId) {

        return ResponseEntity.ok(
                lookbookService.unsaveLookbook(userDetails.getUsername(), lookbookId));
    }

    /**
     * GET /api/user/lookbooks/saved
     * Get all lookbooks saved by the logged-in user.
     */
    @GetMapping("/saved")
    public ResponseEntity<List<LookbookResponse>> getMySavedLookbooks(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                lookbookService.getMySavedLookbooks(userDetails.getUsername()));
    }
}