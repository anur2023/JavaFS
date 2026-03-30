package com.std.ecommerce.module.order.dto;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Long vendorId;
    private String vendorStoreName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private BigDecimal commissionAmount;
    private BigDecimal vendorEarning;

    public OrderItemResponse() {}

    public OrderItemResponse(Long id, Long productId, String productName,
                             Long vendorId, String vendorStoreName,
                             Integer quantity, BigDecimal unitPrice,
                             BigDecimal subtotal, BigDecimal commissionAmount,
                             BigDecimal vendorEarning) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.vendorId = vendorId;
        this.vendorStoreName = vendorStoreName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.commissionAmount = commissionAmount;
        this.vendorEarning = vendorEarning;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public String getVendorStoreName() { return vendorStoreName; }
    public void setVendorStoreName(String vendorStoreName) { this.vendorStoreName = vendorStoreName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }

    public BigDecimal getVendorEarning() { return vendorEarning; }
    public void setVendorEarning(BigDecimal vendorEarning) { this.vendorEarning = vendorEarning; }
}