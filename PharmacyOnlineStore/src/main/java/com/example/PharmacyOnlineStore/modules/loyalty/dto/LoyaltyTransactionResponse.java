package com.example.PharmacyOnlineStore.modules.loyalty.dto;

import com.example.PharmacyOnlineStore.modules.loyalty.entity.LoyaltyTransaction;
import com.example.PharmacyOnlineStore.modules.loyalty.entity.TransactionType;

import java.time.LocalDateTime;

public class LoyaltyTransactionResponse {

    private String transactionId;
    private TransactionType type;
    private int points;
    private String referenceId;
    private String description;
    private int balanceBefore;
    private int balanceAfter;
    private LocalDateTime createdAt;

    public static LoyaltyTransactionResponse from(LoyaltyTransaction t) {
        LoyaltyTransactionResponse res = new LoyaltyTransactionResponse();
        res.transactionId = t.getTransactionId();
        res.type = t.getType();
        res.points = t.getPoints();
        res.referenceId = t.getReferenceId();
        res.description = t.getDescription();
        res.balanceBefore = t.getBalanceBefore();
        res.balanceAfter = t.getBalanceAfter();
        res.createdAt = t.getCreatedAt();
        return res;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public int getPoints() { return points; }
    public String getReferenceId() { return referenceId; }
    public String getDescription() { return description; }
    public int getBalanceBefore() { return balanceBefore; }
    public int getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}