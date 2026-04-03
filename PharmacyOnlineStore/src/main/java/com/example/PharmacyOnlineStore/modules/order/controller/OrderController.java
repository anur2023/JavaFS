package com.example.PharmacyOnlineStore.modules.order.controller;

import com.example.PharmacyOnlineStore.modules.order.dto.*;
import com.example.PharmacyOnlineStore.modules.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // ✅ Constructor Injection (REQUIRED without Lombok)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ─── Place a new order ─────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Get all orders (admin) ────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // ─── Get a specific order by ID ────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // ─── Customer order history ────────────────────────────────────────
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDTO>> getOrderHistory(
            @RequestParam Long customerId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    // ─── Filter orders by status ───────────────────────────────────────
    @GetMapping("/status")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(
            @RequestParam String status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    // ─── Update order status ───────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OrderResponseDTO updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // ─── Update payment status ─────────────────────────────────────────
    @PatchMapping("/{id}/payment")
    public ResponseEntity<OrderResponseDTO> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OrderResponseDTO updated = orderService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // ─── Cancel order ──────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Quick reorder ─────────────────────────────────────────────────
    @PostMapping("/{id}/reorder")
    public ResponseEntity<OrderResponseDTO> quickReorder(@PathVariable Long id) {
        OrderResponseDTO response = orderService.quickReorder(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Get items of an order ─────────────────────────────────────────
    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItems(@PathVariable Long id) {
        List<OrderItemResponseDTO> items = orderService.getItemsByOrderId(id);
        return ResponseEntity.ok(items);
    }
}