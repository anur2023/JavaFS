package com.example.FashionE_CommercewithVirtualTry_On.module.order.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request.PlaceOrderRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.response.OrderResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody PlaceOrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getMyOrders(userDetails.getUsername()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(userDetails.getUsername(), orderId));
    }

    @PostMapping("/{orderId}/reorder")
    public ResponseEntity<OrderResponse> reorder(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.reorder(userDetails.getUsername(), orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(userDetails.getUsername(), orderId));
    }
}