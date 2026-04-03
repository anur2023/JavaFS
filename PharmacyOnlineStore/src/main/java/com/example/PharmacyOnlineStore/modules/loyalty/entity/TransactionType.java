package com.example.PharmacyOnlineStore.modules.loyalty.entity;

public enum TransactionType {
    EARNED,    // Points added (e.g. after order)
    REDEEMED,  // Points used (e.g. discount on order)
    ADJUSTED   // Manual admin correction
}