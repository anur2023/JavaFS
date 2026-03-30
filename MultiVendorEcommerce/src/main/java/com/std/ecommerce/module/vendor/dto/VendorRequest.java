package com.std.ecommerce.module.vendor.dto;

public class VendorRequest {

    private String storeName;
    private String description;

    // Default Constructor
    public VendorRequest() {}

    // Parameterized Constructor
    public VendorRequest(String storeName, String description) {
        this.storeName = storeName;
        this.description = description;
    }

    // Getters and Setters

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}