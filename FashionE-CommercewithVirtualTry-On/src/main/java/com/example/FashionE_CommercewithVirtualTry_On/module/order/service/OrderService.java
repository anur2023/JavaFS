package com.example.FashionE_CommercewithVirtualTry_On.module.order.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository.UserRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request.OrderItemRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request.PlaceOrderRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request.UpdateOrderStatusRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.response.OrderItemResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.response.OrderResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.entity.Order;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.entity.OrderItem;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.entity.OrderStatus;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.entity.PaymentMethod;
import com.example.FashionE_CommercewithVirtualTry_On.module.order.repository.OrderRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems() == null
                ? List.of()
                : order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getOrderItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                )).collect(Collectors.toList());

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setItems(itemResponses);
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethod(order.getPaymentMethod().name());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setDeliveryEstimate(order.getDeliveryEstimate());
        return response;
    }

    @Transactional
    public OrderResponse placeOrder(String email, PlaceOrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must have at least one item");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        order.setDeliveryEstimate(LocalDateTime.now().plusDays(7));

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            items.add(orderItem);

            total += product.getPrice() * itemRequest.getQuantity();
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserUserIdOrderByOrderDateDesc(user.getUserId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(order);
    }

    @Transactional
    public OrderResponse reorder(String email, Long orderId) {
        Order previousOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        PlaceOrderRequest reorderRequest = new PlaceOrderRequest();
        reorderRequest.setShippingAddress(previousOrder.getShippingAddress());
        reorderRequest.setPaymentMethod(previousOrder.getPaymentMethod().name());

        List<OrderItemRequest> items = previousOrder.getOrderItems().stream()
                .map(item -> {
                    OrderItemRequest req = new OrderItemRequest();
                    req.setProductId(item.getProduct().getProductId());
                    req.setQuantity(item.getQuantity());
                    return req;
                }).collect(Collectors.toList());

        reorderRequest.setItems(items);
        return placeOrder(email, reorderRequest);
    }

    @Transactional
    public String cancelOrder(String email, Long orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Access denied");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be cancelled");
        }

        order.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        });

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return "Order cancelled successfully";
    }

    // ─── Admin methods ────────────────────────────────────────────────────────

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByStatus(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return orderRepository.findAll()
                .stream()
                .filter(o -> o.getStatus() == orderStatus)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(request.getStatus().toUpperCase()));

        if (request.getTrackingNumber() != null && !request.getTrackingNumber().isBlank()) {
            order.setTrackingNumber(request.getTrackingNumber());
        }

        return toResponse(orderRepository.save(order));
    }
}