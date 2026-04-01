package com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request;

import java.util.List;

public class PlaceOrderRequest {
    private List<OrderItemRequest> items;
    private String shippingAddress;
    private String paymentMethod;

    public PlaceOrderRequest() {}

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}