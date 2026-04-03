package com.example.PharmacyOnlineStore.modules.medicine.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class MedicineRequest {

    @NotBlank(message = "Medicine name is required")
    private String name;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    private String dosage;

    private String packaging;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    private boolean requiresPrescription = false;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getPackaging() { return packaging; }
    public void setPackaging(String packaging) { this.packaging = packaging; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}