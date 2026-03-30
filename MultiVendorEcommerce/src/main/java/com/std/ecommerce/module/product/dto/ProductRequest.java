package com.std.ecommerce.module.product.dto;

import java.math.BigDecimal;

public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;

    // Foreign key references - we use IDs, not full objects
    private Long vendorId;
    private Long categoryId;

    // 🔹 Default Constructor
    public ProductRequest() {
    }

    // 🔹 Parameterized Constructor
    public ProductRequest(String name, String description, BigDecimal price, Long vendorId, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
    }

    // 🔹 Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}