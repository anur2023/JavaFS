package com.example.PharmacyOnlineStore.modules.inventory.dto;

import com.example.PharmacyOnlineStore.modules.medicine.entity.Medicine;

public class StockSummaryResponse {

    private String medicineId;
    private String medicineName;
    private String categoryName;
    private int currentStock;
    private boolean lowStock; // true if stock <= threshold (e.g. 10)
    private boolean requiresPrescription;

    public static StockSummaryResponse from(Medicine medicine) {
        StockSummaryResponse res = new StockSummaryResponse();
        res.medicineId = medicine.getMedicineId();
        res.medicineName = medicine.getName();
        res.categoryName = medicine.getCategory().getCategoryName();
        res.currentStock = medicine.getStockQuantity();
        res.lowStock = medicine.getStockQuantity() <= 10;
        res.requiresPrescription = medicine.isRequiresPrescription();
        return res;
    }

    public String getMedicineId() { return medicineId; }
    public String getMedicineName() { return medicineName; }
    public String getCategoryName() { return categoryName; }
    public int getCurrentStock() { return currentStock; }
    public boolean isLowStock() { return lowStock; }
    public boolean isRequiresPrescription() { return requiresPrescription; }
}