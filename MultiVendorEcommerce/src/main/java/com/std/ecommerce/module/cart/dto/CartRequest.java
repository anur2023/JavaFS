package com.std.ecommerce.module.cart.dto;

public class CartRequest {

    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;

    // 🔹 Default Constructor
    public CartRequest() {}

    // 🔹 Parameterized Constructor
    public CartRequest(Long productId, String productName, Double productPrice, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    // 🔹 Getters and Setters

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}