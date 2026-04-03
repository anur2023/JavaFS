package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.dto.request;

public class TryOnRequest {

    // ID of the product the user wants to try on
    private Long productId;

    // URL of the user's photo (uploaded externally or via a separate upload endpoint)
    private String userImageUrl;

    public TryOnRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getUserImageUrl() { return userImageUrl; }
    public void setUserImageUrl(String userImageUrl) { this.userImageUrl = userImageUrl; }
}