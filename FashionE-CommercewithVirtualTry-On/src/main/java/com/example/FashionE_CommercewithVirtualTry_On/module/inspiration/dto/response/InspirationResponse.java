package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.response;

import java.time.LocalDateTime;

public class InspirationResponse {

    private Long feedId;
    private String title;
    private String contentText;
    private String imageUrl;
    private LocalDateTime createdAt;

    // 🔹 Default Constructor
    public InspirationResponse() {
    }

    // 🔹 Parameterized Constructor
    public InspirationResponse(Long feedId, String title, String contentText,
                               String imageUrl, LocalDateTime createdAt) {
        this.feedId = feedId;
        this.title = title;
        this.contentText = contentText;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // 🔹 Getters and Setters

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}