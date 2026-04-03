package com.example.PharmacyOnlineStore.modules.auth.dto.response;


import com.example.PharmacyOnlineStore.modules.auth.entity.User;
import java.time.LocalDateTime;

public class UserResponse {

    private String userId;
    private String email;
    private String fullName;
    private String address;
    private String role;
    private Integer loyaltyPoints;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        UserResponse res = new UserResponse();
        res.userId = user.getUserId();
        res.email = user.getEmail();
        res.fullName = user.getFullName();
        res.address = user.getAddress();
        res.role = user.getRole().name();
        res.loyaltyPoints = user.getLoyaltyPoints();
        res.createdAt = user.getCreatedAt();
        return res;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public Integer getLoyaltyPoints() { return loyaltyPoints; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}