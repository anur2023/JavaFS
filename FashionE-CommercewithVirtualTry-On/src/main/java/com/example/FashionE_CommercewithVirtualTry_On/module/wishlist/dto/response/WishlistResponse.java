package com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class WishlistResponse {
    private Long wishlistItemId;
    private Long productId;
    private String productName;
    private Double price;
    private String category;
    private LocalDateTime addedAt;

    public WishlistResponse(Long wishlistId, List<WishlistResponse> itemResponses) {}

    public WishlistResponse(Long wishlistItemId, Long productId, String productName,
                            Double price, String category, LocalDateTime addedAt) {
        this.wishlistItemId = wishlistItemId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.addedAt = addedAt;
    }

    public Long getWishlistItemId() { return wishlistItemId; }
    public void setWishlistItemId(Long wishlistItemId) { this.wishlistItemId = wishlistItemId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}