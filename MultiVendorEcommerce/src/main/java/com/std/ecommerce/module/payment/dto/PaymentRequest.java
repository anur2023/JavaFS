package com.std.ecommerce.module.payment.dto;

public class PaymentRequest {

    private Long orderId;
    private String paymentMethod; // UPI, CARD, COD, NET_BANKING

    public PaymentRequest() {}

    public PaymentRequest(Long orderId, String paymentMethod) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}