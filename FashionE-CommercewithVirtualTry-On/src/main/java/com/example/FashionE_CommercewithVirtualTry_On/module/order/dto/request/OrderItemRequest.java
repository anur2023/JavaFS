package com.example.FashionE_CommercewithVirtualTry_On.module.order.dto.request;

public class OrderItemRequest {
    private Long productId;
    private Integer quantity;

    public OrderItemRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}