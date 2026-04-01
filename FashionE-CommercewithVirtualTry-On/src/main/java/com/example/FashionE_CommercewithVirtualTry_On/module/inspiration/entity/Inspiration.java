package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspiration_feed")
public class Inspiration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String contentText;

    private String imageUrl;

    private LocalDateTime createdAt;

    // 🔹 Default Constructor
    public Inspiration() {
    }

    // 🔹 Parameterized Constructor
    public Inspiration(Long feedId, String title, String contentText,
                       String imageUrl, LocalDateTime createdAt) {
        this.feedId = feedId;
        this.title = title;
        this.contentText = contentText;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // 🔹 Getters & Setters

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