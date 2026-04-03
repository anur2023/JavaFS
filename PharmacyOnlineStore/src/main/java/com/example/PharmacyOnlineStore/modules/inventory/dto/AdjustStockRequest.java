package com.example.PharmacyOnlineStore.modules.inventory.dto;

import com.example.PharmacyOnlineStore.modules.inventory.entity.ChangeType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AdjustStockRequest {

    @NotNull(message = "Medicine ID is required")
    private String medicineId;

    // Positive to add stock, negative to deduct
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    // Only MANUAL_RESTOCK or MANUAL_DEDUCTION allowed via API
    @NotNull(message = "Change type is required")
    private ChangeType changeType;

    private String note;

    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public ChangeType getChangeType() { return changeType; }
    public void setChangeType(ChangeType changeType) { this.changeType = changeType; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}