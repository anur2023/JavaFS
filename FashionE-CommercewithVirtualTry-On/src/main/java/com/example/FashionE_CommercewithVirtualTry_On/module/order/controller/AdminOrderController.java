package com.example.FashionE_CommercewithVirtualTry_On.module.order.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request.UpdateOrderStatusRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.response.OrderResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(required = false) String status) {
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(orderService.getOrdersByStatus(status));
        }
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId,
                                                      @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request));
    }
}