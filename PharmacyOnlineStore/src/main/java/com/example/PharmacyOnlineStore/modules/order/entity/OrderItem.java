package com.example.PharmacyOnlineStore.modules.order.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    // ── Constructors ──────────────────────────────────────────────
    public OrderItem() {}

    public OrderItem(Long orderItemId, Order order, Long medicineId,
                     Integer quantity, BigDecimal unitPrice) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // ── Getters ───────────────────────────────────────────────────
    public Long getOrderItemId() { return orderItemId; }
    public Order getOrder() { return order; }
    public Long getMedicineId() { return medicineId; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    // ── Setters ───────────────────────────────────────────────────
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public void setOrder(Order order) { this.order = order; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long orderItemId;
        private Order order;
        private Long medicineId;
        private Integer quantity;
        private BigDecimal unitPrice;

        public Builder orderItemId(Long orderItemId) { this.orderItemId = orderItemId; return this; }
        public Builder order(Order order) { this.order = order; return this; }
        public Builder medicineId(Long medicineId) { this.medicineId = medicineId; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }

        public OrderItem build() {
            return new OrderItem(orderItemId, order, medicineId, quantity, unitPrice);
        }
    }
}