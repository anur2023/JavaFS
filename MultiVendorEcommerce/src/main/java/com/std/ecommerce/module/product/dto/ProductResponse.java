package com.std.ecommerce.module.product.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    // We include both ID and name for vendor/category to give clients flexibility
    // They can use the ID for updates and display the name for users
    private Long vendorId;
    private String vendorStoreName;

    private Long categoryId;
    private String categoryName;

    // 🔹 Default Constructor
    public ProductResponse() {
    }

    // 🔹 Parameterized Constructor
    public ProductResponse(Long id, String name, String description, BigDecimal price,
                           Long vendorId, String vendorStoreName,
                           Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.vendorId = vendorId;
        this.vendorStoreName = vendorStoreName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // 🔹 Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getVendorStoreName() {
        return vendorStoreName;
    }

    public void setVendorStoreName(String vendorStoreName) {
        this.vendorStoreName = vendorStoreName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}