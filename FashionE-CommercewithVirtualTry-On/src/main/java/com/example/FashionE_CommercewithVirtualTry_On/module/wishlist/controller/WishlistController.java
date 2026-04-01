package com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.dto.request.AddToWishlistRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.dto.response.WishlistResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wishlistService.getWishlist(userDetails.getUsername()));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody AddToWishlistRequest request) {
        return ResponseEntity.ok(wishlistService.addToWishlist(userDetails.getUsername(), request));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromWishlist(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(userDetails.getUsername(), productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wishlistService.clearWishlist(userDetails.getUsername()));
    }
}