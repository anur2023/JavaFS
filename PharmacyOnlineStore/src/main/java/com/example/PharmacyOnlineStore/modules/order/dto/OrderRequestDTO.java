package com.example.PharmacyOnlineStore.modules.order.dto;

import java.util.List;

public class OrderRequestDTO {

    private Long customerId;
    private String deliveryAddress;
    private List<OrderItemRequestDTO> items;

    public OrderRequestDTO() {}

    public Long getCustomerId() { return customerId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public List<OrderItemRequestDTO> getItems() { return items; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setItems(List<OrderItemRequestDTO> items) { this.items = items; }
}