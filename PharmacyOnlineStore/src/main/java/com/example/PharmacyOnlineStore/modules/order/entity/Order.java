package com.example.PharmacyOnlineStore.modules.order.entity;

import com.example.PharmacyOnlineStore.modules.order.enums.OrderStatus;
import com.example.PharmacyOnlineStore.modules.order.enums.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────
    public Order() {}

    public Order(Long orderId, Long customerId, LocalDateTime orderDate,
                 String deliveryAddress, BigDecimal totalAmount,
                 OrderStatus orderStatus, PaymentStatus paymentStatus,
                 List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
    }

    // ── Lifecycle ─────────────────────────────────────────────────
    @PrePersist
    public void prePersist() {
        if (orderDate == null) orderDate = LocalDateTime.now();
        if (orderStatus == null) orderStatus = OrderStatus.PENDING;
        if (paymentStatus == null) paymentStatus = PaymentStatus.PENDING;
    }

    // ── Getters ───────────────────────────────────────────────────
    public Long getOrderId() { return orderId; }
    public Long getCustomerId() { return customerId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    // ── Setters ───────────────────────────────────────────────────
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long orderId;
        private Long customerId;
        private LocalDateTime orderDate;
        private String deliveryAddress;
        private BigDecimal totalAmount;
        private OrderStatus orderStatus;
        private PaymentStatus paymentStatus;
        private List<OrderItem> orderItems = new ArrayList<>();

        public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder orderDate(LocalDateTime orderDate) { this.orderDate = orderDate; return this; }
        public Builder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder orderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder orderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; return this; }

        public Order build() {
            return new Order(orderId, customerId, orderDate, deliveryAddress,
                    totalAmount, orderStatus, paymentStatus, orderItems);
        }
    }
}