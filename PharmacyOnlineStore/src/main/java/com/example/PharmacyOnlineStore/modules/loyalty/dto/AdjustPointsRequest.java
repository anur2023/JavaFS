package com.example.PharmacyOnlineStore.modules.loyalty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdjustPointsRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    // Positive to add, negative to deduct
    @NotNull(message = "Points value is required")
    private Integer points;

    private String description;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}