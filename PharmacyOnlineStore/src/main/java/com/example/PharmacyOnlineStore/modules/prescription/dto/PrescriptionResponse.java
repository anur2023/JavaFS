package com.example.PharmacyOnlineStore.modules.prescription.dto;

import com.example.PharmacyOnlineStore.modules.prescription.entity.Prescription;
import com.example.PharmacyOnlineStore.modules.prescription.entity.ValidationStatus;

import java.time.LocalDateTime;

public class PrescriptionResponse {

    private String prescriptionId;
    private String customerId;
    private String customerName;
    private String medicineId;
    private String medicineName;
    private String imageUrl;
    private ValidationStatus status;
    private String pharmacistNote;
    private String reviewedByName;
    private LocalDateTime uploadedAt;
    private LocalDateTime reviewedAt;

    public static PrescriptionResponse from(Prescription prescription) {
        PrescriptionResponse res = new PrescriptionResponse();
        res.prescriptionId = prescription.getPrescriptionId();
        res.customerId = prescription.getCustomer().getUserId();
        res.customerName = prescription.getCustomer().getFullName();
        res.medicineId = prescription.getMedicine().getMedicineId();
        res.medicineName = prescription.getMedicine().getName();
        res.imageUrl = prescription.getImageUrl();
        res.status = prescription.getStatus();
        res.pharmacistNote = prescription.getPharmacistNote();
        res.reviewedByName = prescription.getReviewedBy() != null
                ? prescription.getReviewedBy().getFullName()
                : null;
        res.uploadedAt = prescription.getUploadedAt();
        res.reviewedAt = prescription.getReviewedAt();
        return res;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getMedicineId() { return medicineId; }
    public String getMedicineName() { return medicineName; }
    public String getImageUrl() { return imageUrl; }
    public ValidationStatus getStatus() { return status; }
    public String getPharmacistNote() { return pharmacistNote; }
    public String getReviewedByName() { return reviewedByName; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
}