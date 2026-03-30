package com.std.ecommerce.module.vendor.entity;

import com.std.ecommerce.module.auth.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Column
    private String description;

    // Default Constructor
    public Vendor() {}

    // Parameterized Constructor
    public Vendor(Long id, User user, String storeName, String description) {
        this.id = id;
        this.user = user;
        this.storeName = storeName;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}