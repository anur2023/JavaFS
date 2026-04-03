package com.example.PharmacyOnlineStore.modules.loyalty.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RedeemPointsRequest {

    @NotNull(message = "Points to redeem is required")
    @Min(value = 1, message = "Must redeem at least 1 point")
    private Integer points;

    // Optional: orderId this redemption is applied to
    private String orderId;

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}