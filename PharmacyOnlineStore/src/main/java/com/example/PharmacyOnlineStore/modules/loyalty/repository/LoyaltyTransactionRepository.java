package com.example.PharmacyOnlineStore.modules.loyalty.repository;

import com.example.PharmacyOnlineStore.modules.loyalty.entity.LoyaltyTransaction;
import com.example.PharmacyOnlineStore.modules.loyalty.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, String> {

    // All transactions for a user (paginated)
    Page<LoyaltyTransaction> findByUserUserId(String userId, Pageable pageable);

    // Filter by type for a user
    Page<LoyaltyTransaction> findByUserUserIdAndType(String userId, TransactionType type, Pageable pageable);

    // Total points earned by a user
    @Query("SELECT COALESCE(SUM(t.points), 0) FROM LoyaltyTransaction t " +
            "WHERE t.user.userId = :userId AND t.type = 'EARNED'")
    int sumEarnedPoints(@Param("userId") String userId);

    // Total points redeemed by a user
    @Query("SELECT COALESCE(SUM(t.points), 0) FROM LoyaltyTransaction t " +
            "WHERE t.user.userId = :userId AND t.type = 'REDEEMED'")
    int sumRedeemedPoints(@Param("userId") String userId);

    // Check if points were already earned for a specific order
    boolean existsByReferenceIdAndType(String referenceId, TransactionType type);
}