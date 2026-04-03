package com.example.PharmacyOnlineStore.modules.payment.dto;

import com.example.PharmacyOnlineStore.modules.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentId;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionReference;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentResponse from(Payment payment) {
        PaymentResponse res = new PaymentResponse();
        res.paymentId = payment.getPaymentId();
        res.orderId = payment.getOrderId();
        res.userId = payment.getUserId();
        res.amount = payment.getAmount();
        res.paymentMethod = payment.getPaymentMethod().name();
        res.paymentStatus = payment.getPaymentStatus().name();
        res.transactionReference = payment.getTransactionReference();
        res.failureReason = payment.getFailureReason();
        res.createdAt = payment.getCreatedAt();
        res.updatedAt = payment.getUpdatedAt();
        return res;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getTransactionReference() { return transactionReference; }
    public String getFailureReason() { return failureReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}