package com.example.PharmacyOnlineStore.modules.medicine.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String medicineId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String dosage;

    private String packaging;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean requiresPrescription = false;

    @Column(nullable = false)
    private Integer stockQuantity = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

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