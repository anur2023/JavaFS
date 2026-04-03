package com.example.PharmacyOnlineStore.modules.loyalty.controller;

import com.example.PharmacyOnlineStore.modules.loyalty.dto.*;
import com.example.PharmacyOnlineStore.modules.loyalty.service.LoyaltyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    // ─── CUSTOMER endpoints ────────────────────────────────────────────────────

    /**
     * GET /api/loyalty/balance
     * Customer checks their current points balance.
     */
    @GetMapping("/balance")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoyaltyBalanceResponse> getBalance(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(loyaltyService.getBalance(userDetails.getUsername()));
    }

    /**
     * POST /api/loyalty/redeem
     * Customer redeems points for a discount.
     * Body: { points, orderId (optional) }
     */
    @PostMapping("/redeem")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, Object>> redeemPoints(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody RedeemPointsRequest request) {

        double discount = loyaltyService.redeemPoints(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of(
                "message", "Points redeemed successfully",
                "pointsRedeemed", request.getPoints(),
                "discountApplied", "₹" + String.format("%.2f", discount)
        ));
    }

    /**
     * GET /api/loyalty/history
     * Customer views their own transaction history.
     */
    @GetMapping("/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<LoyaltyTransactionResponse>> getMyHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(loyaltyService.getMyHistory(userDetails.getUsername(), pageable));
    }

    // ─── ADMIN endpoints ───────────────────────────────────────────────────────

    /**
     * GET /api/loyalty/admin/balance/{userId}
     * Admin checks any user's loyalty balance.
     */
    @GetMapping("/admin/balance/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoyaltyBalanceResponse> getUserBalance(@PathVariable String userId) {
        return ResponseEntity.ok(loyaltyService.getBalanceForUser(userId));
    }

    /**
     * GET /api/loyalty/admin/history/{userId}
     * Admin views any user's transaction history.
     */
    @GetMapping("/admin/history/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<LoyaltyTransactionResponse>> getUserHistory(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(loyaltyService.getUserHistory(userId, pageable));
    }

    /**
     * POST /api/loyalty/admin/adjust
     * Admin manually adds or deducts points from a user.
     * Body: { userId, points (positive/negative), description }
     */
    @PostMapping("/admin/adjust")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoyaltyTransactionResponse> adjustPoints(
            @Valid @RequestBody AdjustPointsRequest request) {
        return ResponseEntity.ok(loyaltyService.adjustPoints(request));
    }
}