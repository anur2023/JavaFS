package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.entity;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tryon_sessions")
public class TryOnSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // URL of the photo uploaded by the user
    @Column(name = "user_image_url", nullable = false, length = 1000)
    private String userImageUrl;

    // URL of the AR-overlaid result image returned by the external service
    @Column(name = "result_image_url", length = 1000)
    private String resultImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TryOnStatus status;

    // Informational / error message from the AR API
    @Column(name = "api_message", length = 500)
    private String apiMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public TryOnSession() {}

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getUserImageUrl() { return userImageUrl; }
    public void setUserImageUrl(String userImageUrl) { this.userImageUrl = userImageUrl; }

    public String getResultImageUrl() { return resultImageUrl; }
    public void setResultImageUrl(String resultImageUrl) { this.resultImageUrl = resultImageUrl; }

    public TryOnStatus getStatus() { return status; }
    public void setStatus(TryOnStatus status) { this.status = status; }

    public String getApiMessage() { return apiMessage; }
    public void setApiMessage(String apiMessage) { this.apiMessage = apiMessage; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}