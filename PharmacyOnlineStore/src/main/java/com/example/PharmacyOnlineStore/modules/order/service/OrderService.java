package com.example.PharmacyOnlineStore.modules.order.service;

import com.example.PharmacyOnlineStore.modules.order.dto.*;
import com.example.PharmacyOnlineStore.modules.order.entity.Order;
import com.example.PharmacyOnlineStore.modules.order.entity.OrderItem;
import com.example.PharmacyOnlineStore.modules.order.enums.OrderStatus;
import com.example.PharmacyOnlineStore.modules.order.enums.PaymentStatus;
import com.example.PharmacyOnlineStore.modules.order.repository.OrderItemRepository;
import com.example.PharmacyOnlineStore.modules.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {
        List<OrderItem> items = request.getItems().stream().map(dto -> {
            OrderItem item = new OrderItem();
            item.setMedicineId(dto.getMedicineId());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            return item;
        }).collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setTotalAmount(total);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        items.forEach(item -> item.setOrder(order));
        order.getOrderItems().addAll(items);

        return toResponseDTO(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return toResponseDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByStatus(String status) {
        return orderRepository
                .findByOrderStatus(OrderStatus.valueOf(status.toUpperCase()))
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));
        return toResponseDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDTO updatePaymentStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setPaymentStatus(PaymentStatus.valueOf(status.toUpperCase()));
        return toResponseDTO(orderRepository.save(order));
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be cancelled.");
        }
        orderRepository.delete(order);
    }

    @Transactional
    public OrderResponseDTO quickReorder(Long previousOrderId) {
        Order previous = orderRepository.findById(previousOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + previousOrderId));

        Order newOrder = new Order();
        newOrder.setCustomerId(previous.getCustomerId());
        newOrder.setDeliveryAddress(previous.getDeliveryAddress());
        newOrder.setTotalAmount(previous.getTotalAmount());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setPaymentStatus(PaymentStatus.PENDING);

        List<OrderItem> newItems = previous.getOrderItems().stream().map(old -> {
            OrderItem item = new OrderItem();
            item.setMedicineId(old.getMedicineId());
            item.setQuantity(old.getQuantity());
            item.setUnitPrice(old.getUnitPrice());
            item.setOrder(newOrder);
            return item;
        }).collect(Collectors.toList());

        newOrder.getOrderItems().addAll(newItems);
        return toResponseDTO(orderRepository.save(newOrder));
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponseDTO> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderOrderId(orderId)
                .stream().map(this::toItemResponseDTO).collect(Collectors.toList());
    }

    // ── Mapping helpers ───────────────────────────────────────────
    private OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setItems(order.getOrderItems().stream()
                .map(this::toItemResponseDTO).collect(Collectors.toList()));
        return dto;
    }

    private OrderItemResponseDTO toItemResponseDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setMedicineId(item.getMedicineId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }
}