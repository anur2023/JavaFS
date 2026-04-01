package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.entity.TryOnSession;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.entity.TryOnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TryOnSessionRepository extends JpaRepository<TryOnSession, Long> {

    // All sessions for a user, newest first
    List<TryOnSession> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    // Sessions for a user filtered by status
    List<TryOnSession> findByUserUserIdAndStatus(Long userId, TryOnStatus status);

    // Single session scoped to a user (prevents other users from viewing it)
    Optional<TryOnSession> findBySessionIdAndUserUserId(Long sessionId, Long userId);

    // All sessions for a specific product (admin use)
    List<TryOnSession> findByProductProductId(Long productId);
}