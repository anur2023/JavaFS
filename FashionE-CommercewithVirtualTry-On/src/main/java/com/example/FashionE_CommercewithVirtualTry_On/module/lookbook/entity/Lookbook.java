package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lookbooks")
public class Lookbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lookbookId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    // Season tag e.g. "Summer 2025", "Winter 2024"
    @Column
    private String season;

    // Style tag e.g. "Casual", "Formal", "Boho"
    @Column
    private String style;

    // Cover image URL for the lookbook
    @Column(name = "cover_image_url", length = 1000)
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LookbookStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "lookbook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LookbookItem> items = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Lookbook() {}

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

    public LookbookStatus getStatus() { return status; }
    public void setStatus(LookbookStatus status) { this.status = status; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<LookbookItem> getItems() { return items; }
    public void setItems(List<LookbookItem> items) { this.items = items; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}