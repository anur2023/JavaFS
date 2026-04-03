package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class LookbookResponse {

    private Long lookbookId;
    private String title;
    private String description;
    private String season;
    private String style;
    private String coverImageUrl;
    private String status;
    private String createdByEmail;
    private List<LookbookItemResponse> items;
    private long savedCount;        // how many users bookmarked this lookbook
    private boolean savedByMe;      // true when the logged-in customer has saved it
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LookbookResponse() {}

    public Long getLookbookId() { return lookbookId; }
    public void setLookbookId(Long lookbookId) { this.lookbookId = lookbookId; }

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

    public String getCreatedByEmail() { return createdByEmail; }
    public void setCreatedByEmail(String createdByEmail) { this.createdByEmail = createdByEmail; }

    public List<LookbookItemResponse> getItems() { return items; }
    public void setItems(List<LookbookItemResponse> items) { this.items = items; }

    public long getSavedCount() { return savedCount; }
    public void setSavedCount(long savedCount) { this.savedCount = savedCount; }

    public boolean isSavedByMe() { return savedByMe; }
    public void setSavedByMe(boolean savedByMe) { this.savedByMe = savedByMe; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}