package com.std.ecommerce.module.cart.controller;

import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.cart.dto.CartRequest;
import com.std.ecommerce.module.cart.dto.CartResponse;
import com.std.ecommerce.module.cart.dto.CartSummaryResponse;
import com.std.ecommerce.module.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class Cartcontroller {

    @Autowired
    private CartService cartService;

    // 🔥 ADD item to cart
    // 🔐 Requires: CUSTOMER token
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal User user,
            @RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(user.getId(), request));
    }

    // 🔥 GET my cart
    // 🔐 Requires: any logged-in user token
    @GetMapping
    public ResponseEntity<CartSummaryResponse> getMyCart(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user.getId()));
    }

    // 🔥 UPDATE quantity of a cart item
    // 🔐 Requires: any logged-in user token
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(user.getId(), cartItemId, quantity));
    }

    // 🔥 REMOVE single item from cart
    // 🔐 Requires: any logged-in user token
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeCartItem(user.getId(), cartItemId));
    }

    // 🔥 CLEAR entire cart
    // 🔐 Requires: any logged-in user token
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.clearCart(user.getId()));
    }
}