package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request;

import java.util.List;

public class CreateLookbookRequest {

    private String title;
    private String description;
    private String season;
    private String style;
    private String coverImageUrl;
    // Optional: seed product IDs at creation time
    private List<Long> productIds;

    public CreateLookbookRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
}