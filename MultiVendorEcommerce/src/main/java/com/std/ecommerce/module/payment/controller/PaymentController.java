package com.std.ecommerce.module.payment.controller;

import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.payment.dto.PaymentRequest;
import com.std.ecommerce.module.payment.dto.PaymentResponse;
import com.std.ecommerce.module.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // 🔥 INITIATE payment
    // 🔐 Authenticated user
    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(
            @AuthenticationPrincipal User user,
            @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.initiatePayment(user.getId(), request));
    }

    // 🔥 GET payment for an order
    // 🔐 Authenticated user (ownership checked in service)
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderId, user.getId()));
    }
}