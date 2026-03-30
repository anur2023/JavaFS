package com.std.ecommerce.module.vendor.dto;

public class VendorResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String storeName;
    private String description;

    // Default Constructor
    public VendorResponse() {}

    // Parameterized Constructor
    public VendorResponse(Long id, Long userId, String userName, String userEmail,
                          String storeName, String description) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.storeName = storeName;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}