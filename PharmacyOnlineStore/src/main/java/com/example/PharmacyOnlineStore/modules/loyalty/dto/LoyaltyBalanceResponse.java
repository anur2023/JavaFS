package com.example.PharmacyOnlineStore.modules.loyalty.dto;

public class LoyaltyBalanceResponse {

    private String userId;
    private String fullName;
    private int currentPoints;
    private double rupeeValue; // 1 point = ₹0.50 (configurable)

    public LoyaltyBalanceResponse(String userId, String fullName, int currentPoints) {
        this.userId = userId;
        this.fullName = fullName;
        this.currentPoints = currentPoints;
        this.rupeeValue = currentPoints * 0.50;
    }

    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public int getCurrentPoints() { return currentPoints; }
    public double getRupeeValue() { return rupeeValue; }
}