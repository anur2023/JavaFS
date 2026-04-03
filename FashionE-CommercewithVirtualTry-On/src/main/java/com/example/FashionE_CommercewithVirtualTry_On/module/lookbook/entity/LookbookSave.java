package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lookbook_saves", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lookbook_id", "user_id"})
})
public class LookbookSave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookbook_id", nullable = false)
    private Lookbook lookbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    public LookbookSave() {}

    public Long getSaveId() { return saveId; }
    public void setSaveId(Long saveId) { this.saveId = saveId; }

    public Lookbook getLookbook() { return lookbook; }
    public void setLookbook(Lookbook lookbook) { this.lookbook = lookbook; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }
}