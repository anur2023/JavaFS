package com.std.ecommerce.module.cart.dto;

public class CartResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice; // productPrice * quantity

    // 🔹 Default Constructor
    public CartResponse() {}

    // 🔹 Parameterized Constructor
    public CartResponse(Long id, Long userId, Long productId, String productName,
                        Double productPrice, Integer quantity, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // 🔹 Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}