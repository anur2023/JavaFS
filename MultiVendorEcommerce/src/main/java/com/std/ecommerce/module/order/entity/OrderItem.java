package com.std.ecommerce.module.order.entity;

import com.std.ecommerce.module.product.entity.Product;
import com.std.ecommerce.module.vendor.entity.Vendor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Snapshot of vendor at time of order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private Integer quantity;

    // Price snapshot at time of order (price can change later)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    // Commission deducted from vendor (e.g. 10% platform fee)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionAmount;

    // What vendor actually receives
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal vendorEarning;

    public OrderItem() {}

    public OrderItem(Long id, Order order, Product product, Vendor vendor,
                     Integer quantity, BigDecimal unitPrice,
                     BigDecimal commissionAmount, BigDecimal vendorEarning) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.vendor = vendor;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.commissionAmount = commissionAmount;
        this.vendorEarning = vendorEarning;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }

    public BigDecimal getVendorEarning() { return vendorEarning; }
    public void setVendorEarning(BigDecimal vendorEarning) { this.vendorEarning = vendorEarning; }
}