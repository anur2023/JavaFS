package com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request;

public class UpdateOrderStatusRequest {
    private String status;
    private String trackingNumber;

    public UpdateOrderStatusRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}