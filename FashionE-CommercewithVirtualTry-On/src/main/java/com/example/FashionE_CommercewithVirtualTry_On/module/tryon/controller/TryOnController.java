package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.dto.request.TryOnRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.dto.response.TryOnResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.service.TryOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User-facing Virtual Try-On endpoints.
 * Base URL : /api/user/tryon
 * Secured  : ROLE_CUSTOMER  (via SecurityConfig — /api/user/** requires CUSTOMER role)
 */
@RestController
@RequestMapping("/api/user/tryon")
public class TryOnController {

    @Autowired
    private TryOnService tryOnService;

    /**
     * POST /api/user/tryon
     * Start a new virtual try-on session.
     * Body: { "productId": 1, "userImageUrl": "https://..." }
     */
    @PostMapping
    public ResponseEntity<TryOnResponse> startTryOn(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TryOnRequest request) {

        TryOnResponse response = tryOnService.startTryOn(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/user/tryon
     * Get full try-on history for the logged-in user.
     */
    @GetMapping
    public ResponseEntity<List<TryOnResponse>> getMyHistory(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(tryOnService.getMyTryOnHistory(userDetails.getUsername()));
    }

    /**
     * GET /api/user/tryon/{sessionId}
     * Get a specific try-on session (must belong to the logged-in user).
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<TryOnResponse> getSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sessionId) {

        return ResponseEntity.ok(tryOnService.getSessionById(userDetails.getUsername(), sessionId));
    }

    /**
     * DELETE /api/user/tryon/{sessionId}
     * Remove a try-on session from the user's history.
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<String> deleteSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sessionId) {

        return ResponseEntity.ok(tryOnService.deleteSession(userDetails.getUsername(), sessionId));
    }
}