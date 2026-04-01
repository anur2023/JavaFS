package com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.entity;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Wishlist() {}

    public Long getWishlistId() { return wishlistId; }
    public void setWishlistId(Long wishlistId) { this.wishlistId = wishlistId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WishlistItem> getItems() { return items; }
    public void setItems(List<WishlistItem> items) { this.items = items; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}