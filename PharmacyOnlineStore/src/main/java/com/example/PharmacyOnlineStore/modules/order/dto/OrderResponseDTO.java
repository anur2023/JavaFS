package com.example.PharmacyOnlineStore.modules.order.dto;

import com.example.PharmacyOnlineStore.modules.order.enums.OrderStatus;
import com.example.PharmacyOnlineStore.modules.order.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String deliveryAddress;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private List<OrderItemResponseDTO> items;

    public OrderResponseDTO() {}

    public OrderResponseDTO(Long orderId, Long customerId, LocalDateTime orderDate,
                            String deliveryAddress, BigDecimal totalAmount,
                            OrderStatus orderStatus, PaymentStatus paymentStatus,
                            List<OrderItemResponseDTO> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.items = items;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public Long getCustomerId() { return customerId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public List<OrderItemResponseDTO> getItems() { return items; }

    // Setters
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setItems(List<OrderItemResponseDTO> items) { this.items = items; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long orderId;
        private Long customerId;
        private LocalDateTime orderDate;
        private String deliveryAddress;
        private BigDecimal totalAmount;
        private OrderStatus orderStatus;
        private PaymentStatus paymentStatus;
        private List<OrderItemResponseDTO> items;

        public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder orderDate(LocalDateTime orderDate) { this.orderDate = orderDate; return this; }
        public Builder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder orderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder items(List<OrderItemResponseDTO> items) { this.items = items; return this; }

        public OrderResponseDTO build() {
            return new OrderResponseDTO(orderId, customerId, orderDate, deliveryAddress,
                    totalAmount, orderStatus, paymentStatus, items);
        }
    }
}