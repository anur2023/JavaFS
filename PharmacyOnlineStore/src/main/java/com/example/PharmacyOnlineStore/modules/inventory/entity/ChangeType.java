package com.example.PharmacyOnlineStore.modules.inventory.entity;

public enum ChangeType {
    ORDER_DEDUCTION,     // Stock reduced after confirmed order
    MANUAL_RESTOCK,      // Admin/Pharmacist manually adds stock
    MANUAL_DEDUCTION,    // Admin/Pharmacist manually removes stock
    ORDER_CANCELLATION   // Stock restored after order is cancelled
}