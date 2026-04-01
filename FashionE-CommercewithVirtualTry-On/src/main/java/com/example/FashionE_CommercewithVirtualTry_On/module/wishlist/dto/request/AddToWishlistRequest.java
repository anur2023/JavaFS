package com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.dto.request;

public class AddToWishlistRequest {
    private Long productId;

    public AddToWishlistRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}