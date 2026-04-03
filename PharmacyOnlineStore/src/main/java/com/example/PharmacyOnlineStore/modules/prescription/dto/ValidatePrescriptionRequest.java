package com.example.PharmacyOnlineStore.modules.prescription.dto;

import com.example.PharmacyOnlineStore.modules.prescription.entity.ValidationStatus;
import jakarta.validation.constraints.NotNull;

public class ValidatePrescriptionRequest {

    @NotNull(message = "Status is required")
    private ValidationStatus status; // APPROVED or REJECTED

    private String pharmacistNote;

    public ValidationStatus getStatus() { return status; }
    public void setStatus(ValidationStatus status) { this.status = status; }

    public String getPharmacistNote() { return pharmacistNote; }
    public void setPharmacistNote(String pharmacistNote) { this.pharmacistNote = pharmacistNote; }
}