package com.example.PharmacyOnlineStore.modules.inventory.dto;

import com.example.PharmacyOnlineStore.modules.inventory.entity.ChangeType;
import com.example.PharmacyOnlineStore.modules.inventory.entity.InventoryLog;

import java.time.LocalDateTime;

public class InventoryLogResponse {

    private String logId;
    private String medicineId;
    private String medicineName;
    private ChangeType changeType;
    private int quantityChanged;
    private int stockBefore;
    private int stockAfter;
    private String referenceId;
    private String note;
    private String performedByName;
    private LocalDateTime createdAt;

    public static InventoryLogResponse from(InventoryLog log) {
        InventoryLogResponse res = new InventoryLogResponse();
        res.logId = log.getLogId();
        res.medicineId = log.getMedicine().getMedicineId();
        res.medicineName = log.getMedicine().getName();
        res.changeType = log.getChangeType();
        res.quantityChanged = log.getQuantityChanged();
        res.stockBefore = log.getStockBefore();
        res.stockAfter = log.getStockAfter();
        res.referenceId = log.getReferenceId();
        res.note = log.getNote();
        res.performedByName = log.getPerformedBy() != null
                ? log.getPerformedBy().getFullName()
                : "System";
        res.createdAt = log.getCreatedAt();
        return res;
    }

    public String getLogId() { return logId; }
    public String getMedicineId() { return medicineId; }
    public String getMedicineName() { return medicineName; }
    public ChangeType getChangeType() { return changeType; }
    public int getQuantityChanged() { return quantityChanged; }
    public int getStockBefore() { return stockBefore; }
    public int getStockAfter() { return stockAfter; }
    public String getReferenceId() { return referenceId; }
    public String getNote() { return note; }
    public String getPerformedByName() { return performedByName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}