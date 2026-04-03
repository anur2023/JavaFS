package com.example.PharmacyOnlineStore.modules.order.dto;

import java.math.BigDecimal;

public class OrderItemRequestDTO {

    private Long medicineId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItemRequestDTO() {}

    public Long getMedicineId() { return medicineId; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}