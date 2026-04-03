package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request;

public class UpdateLookbookRequest {

    private String title;
    private String description;
    private String season;
    private String style;
    private String coverImageUrl;
    // "DRAFT" | "PUBLISHED" | "ARCHIVED"
    private String status;

    public UpdateLookbookRequest() {}

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}