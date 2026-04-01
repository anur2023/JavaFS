package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request;

public class AddLookbookItemRequest {

    private Long productId;
    private String stylingNote;
    private Integer displayOrder;

    public AddLookbookItemRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getStylingNote() { return stylingNote; }
    public void setStylingNote(String stylingNote) { this.stylingNote = stylingNote; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}