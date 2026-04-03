package com.example.PharmacyOnlineStore.modules.prescription.dto;

import jakarta.validation.constraints.NotBlank;

public class UploadPrescriptionRequest {

    @NotBlank(message = "Medicine ID is required")
    private String medicineId;

    // The image URL after the frontend has uploaded it to Supabase Storage
    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}