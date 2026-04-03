package com.example.PharmacyOnlineStore.modules.order.dto;

import java.math.BigDecimal;

public class OrderItemResponseDTO {

    private Long orderItemId;
    private Long medicineId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderItemResponseDTO() {}

    public OrderItemResponseDTO(Long orderItemId, Long medicineId,
                                Integer quantity, BigDecimal unitPrice,
                                BigDecimal subtotal) {
        this.orderItemId = orderItemId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // Getters
    public Long getOrderItemId() { return orderItemId; }
    public Long getMedicineId() { return medicineId; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }

    // Setters
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long orderItemId;
        private Long medicineId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;

        public Builder orderItemId(Long orderItemId) { this.orderItemId = orderItemId; return this; }
        public Builder medicineId(Long medicineId) { this.medicineId = medicineId; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public Builder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }

        public OrderItemResponseDTO build() {
            return new OrderItemResponseDTO(orderItemId, medicineId,
                    quantity, unitPrice, subtotal);
        }
    }
}