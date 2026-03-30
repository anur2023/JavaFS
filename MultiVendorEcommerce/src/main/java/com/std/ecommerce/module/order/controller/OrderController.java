package com.std.ecommerce.module.order.controller;

import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.order.dto.OrderItemResponse;
import com.std.ecommerce.module.order.dto.OrderRequest;
import com.std.ecommerce.module.order.dto.OrderResponse;
import com.std.ecommerce.module.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 🔥 PLACE ORDER
    // 🔐 Any authenticated user (typically CUSTOMER)
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(user.getId(), request));
    }

    // 🔥 GET my orders
    // 🔐 Authenticated user
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getMyOrders(user.getId()));
    }

    // 🔥 GET single order by ID
    // 🔐 Authenticated user (ownership checked in service)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, user.getId()));
    }

    // 🔥 GET ALL orders (Admin)
    // 🔐 ADMIN only
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 🔥 GET items sold by a vendor
    // 🔐 VENDOR only
    @GetMapping("/vendor/{vendorId}/items")
    public ResponseEntity<List<OrderItemResponse>> getOrdersByVendor(
            @PathVariable Long vendorId) {
        return ResponseEntity.ok(orderService.getOrdersByVendor(vendorId));
    }

    // 🔥 CANCEL order
    // 🔐 Authenticated user (ownership checked in service)
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, user.getId()));
    }
}