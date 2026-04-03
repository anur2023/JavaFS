package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.response;

public class LookbookItemResponse {

    private Long lookbookItemId;
    private Long productId;
    private String productName;
    private String productCategory;
    private Double productPrice;
    private String productImageUrl;   // primary image of the product
    private String stylingNote;
    private Integer displayOrder;

    public LookbookItemResponse() {}

    public Long getLookbookItemId() { return lookbookItemId; }
    public void setLookbookItemId(Long lookbookItemId) { this.lookbookItemId = lookbookItemId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getStylingNote() { return stylingNote; }
    public void setStylingNote(String stylingNote) { this.stylingNote = stylingNote; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}