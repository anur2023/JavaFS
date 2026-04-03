package com.example.PharmacyOnlineStore.modules.medicine.dto;

import com.example.PharmacyOnlineStore.modules.medicine.entity.Medicine;
import java.math.BigDecimal;

public class MedicineResponse {

    private String medicineId;
    private String name;
    private String categoryId;
    private String categoryName;
    private String dosage;
    private String packaging;
    private BigDecimal price;
    private boolean requiresPrescription;
    private Integer stockQuantity;
    private String description;

    public static MedicineResponse from(Medicine medicine) {
        MedicineResponse res = new MedicineResponse();
        res.medicineId = medicine.getMedicineId();
        res.name = medicine.getName();
        res.categoryId = medicine.getCategory().getCategoryId();
        res.categoryName = medicine.getCategory().getCategoryName();
        res.dosage = medicine.getDosage();
        res.packaging = medicine.getPackaging();
        res.price = medicine.getPrice();
        res.requiresPrescription = medicine.isRequiresPrescription();
        res.stockQuantity = medicine.getStockQuantity();
        res.description = medicine.getDescription();
        return res;
    }

    public String getMedicineId() { return medicineId; }
    public String getName() { return name; }
    public String getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getDosage() { return dosage; }
    public String getPackaging() { return packaging; }
    public BigDecimal getPrice() { return price; }
    public boolean isRequiresPrescription() { return requiresPrescription; }
    public Integer getStockQuantity() { return stockQuantity; }
    public String getDescription() { return description; }
}