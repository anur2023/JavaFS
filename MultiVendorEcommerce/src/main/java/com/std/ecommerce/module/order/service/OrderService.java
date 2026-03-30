package com.std.ecommerce.module.order.service;

import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.auth.repository.UserRepository;
import com.std.ecommerce.module.order.dto.*;
import com.std.ecommerce.module.order.entity.Order;
import com.std.ecommerce.module.order.entity.OrderItem;
import com.std.ecommerce.module.order.repository.OrderRepo;
import com.std.ecommerce.module.order.repository.OrderItemRepo;
import com.std.ecommerce.module.product.entity.Product;
import com.std.ecommerce.module.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    // Platform commission rate: 10%
    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.10");

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // 🔥 PLACE ORDER — handles multi-vendor cart in one transaction
    @Transactional
    public OrderResponse placeOrder(Long userId, OrderRequest request) {

        // 1. Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        // 2. Create the Order shell first
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO); // will update after items
        Order savedOrder = orderRepo.save(order);

        // 3. Process each item — supports products from different vendors
        BigDecimal grandTotal = BigDecimal.ZERO;
        List<OrderItem> savedItems = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id: " + itemReq.getProductId()));

            if (itemReq.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be at least 1 for product: " + product.getName());
            }

            BigDecimal unitPrice   = product.getPrice();
            BigDecimal subtotal    = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            BigDecimal commission  = subtotal.multiply(COMMISSION_RATE).setScale(2, RoundingMode.HALF_UP);
            BigDecimal vendorEarn  = subtotal.subtract(commission);

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setVendor(product.getVendor()); // auto-resolved from product
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setCommissionAmount(commission);
            item.setVendorEarning(vendorEarn);

            savedItems.add(orderItemRepo.save(item));
            grandTotal = grandTotal.add(subtotal);
        }

        // 4. Update order total and confirm
        savedOrder.setTotalAmount(grandTotal);
        savedOrder.setStatus("CONFIRMED");
        orderRepo.save(savedOrder);

        return toResponse(savedOrder, savedItems);
    }

    // 🔥 GET my orders (Customer)
    public List<OrderResponse> getMyOrders(Long userId) {
        return orderRepo.findByUserId(userId)
                .stream()
                .map(order -> toResponse(order, orderItemRepo.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    // 🔥 GET single order by ID
    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Security: customer can only see their own order
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This order does not belong to you");
        }

        return toResponse(order, orderItemRepo.findByOrderId(orderId));
    }

    // 🔥 GET ALL orders (Admin only)
    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(order -> toResponse(order, orderItemRepo.findByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    // 🔥 GET orders by vendor (Vendor sees their sold items)
    public List<OrderItemResponse> getOrdersByVendor(Long vendorId) {
        return orderItemRepo.findByVendorId(vendorId)
                .stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
    }

    // 🔥 CANCEL order
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This order does not belong to you");
        }

        if (order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Order is already cancelled");
        }

        order.setStatus("CANCELLED");
        orderRepo.save(order);

        return toResponse(order, orderItemRepo.findByOrderId(orderId));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemResponses
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        BigDecimal subtotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getVendor().getId(),
                item.getVendor().getStoreName(),
                item.getQuantity(),
                item.getUnitPrice(),
                subtotal,
                item.getCommissionAmount(),
                item.getVendorEarning()
        );
    }
}