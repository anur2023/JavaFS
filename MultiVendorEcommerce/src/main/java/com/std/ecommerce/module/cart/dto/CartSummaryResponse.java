package com.std.ecommerce.module.cart.dto;

import java.util.List;

public class CartSummaryResponse {

    private List<CartResponse> items;
    private int totalItems;
    private Double grandTotal;

    // 🔹 Default Constructor
    public CartSummaryResponse() {}

    // 🔹 Parameterized Constructor
    public CartSummaryResponse(List<CartResponse> items, int totalItems, Double grandTotal) {
        this.items = items;
        this.totalItems = totalItems;
        this.grandTotal = grandTotal;
    }

    // 🔹 Getters and Setters

    public List<CartResponse> getItems() { return items; }
    public void setItems(List<CartResponse> items) { this.items = items; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public Double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Double grandTotal) { this.grandTotal = grandTotal; }
}