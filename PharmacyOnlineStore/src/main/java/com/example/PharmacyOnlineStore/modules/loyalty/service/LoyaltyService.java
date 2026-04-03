package com.example.PharmacyOnlineStore.modules.loyalty.service;

import com.example.PharmacyOnlineStore.common.exception.ResourceNotFoundException;
import com.example.PharmacyOnlineStore.modules.auth.entity.User;
import com.example.PharmacyOnlineStore.modules.auth.repository.UserRepository;
import com.example.PharmacyOnlineStore.modules.loyalty.dto.*;
import com.example.PharmacyOnlineStore.modules.loyalty.entity.LoyaltyTransaction;
import com.example.PharmacyOnlineStore.modules.loyalty.entity.TransactionType;
import com.example.PharmacyOnlineStore.modules.loyalty.repository.LoyaltyTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoyaltyService {

    // 1 point earned per ₹10 spent
    private static final double POINTS_PER_RUPEE = 0.1;
    // 1 point = ₹0.50 when redeemed
    private static final double RUPEE_VALUE_PER_POINT = 0.50;

    private final LoyaltyTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public LoyaltyService(LoyaltyTransactionRepository transactionRepository,
                          UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // ─── Internal methods (called by OrderService) ─────────────────────────────

    /**
     * Award points after a confirmed order.
     * Called by OrderService — idempotent (won't double-award same order).
     */
    @Transactional
    public void awardPointsForOrder(String userId, double orderAmount, String orderId) {
        // Prevent duplicate awarding for the same order
        if (transactionRepository.existsByReferenceIdAndType(orderId, TransactionType.EARNED)) {
            return;
        }

        User user = findUserById(userId);
        int pointsToAward = (int) Math.floor(orderAmount * POINTS_PER_RUPEE);
        if (pointsToAward <= 0) return;

        int before = user.getLoyaltyPoints();
        int after = before + pointsToAward;

        user.setLoyaltyPoints(after);
        userRepository.save(user);

        saveTransaction(user, TransactionType.EARNED, pointsToAward, before, after,
                orderId, "Points earned for order #" + orderId);
    }

    /**
     * Reverse points if an order is cancelled (only if they were earned).
     * Called by OrderService.
     */
    @Transactional
    public void reversePointsForCancellation(String userId, double orderAmount, String orderId) {
        if (!transactionRepository.existsByReferenceIdAndType(orderId, TransactionType.EARNED)) {
            return; // Nothing was awarded, nothing to reverse
        }

        User user = findUserById(userId);
        int pointsToReverse = (int) Math.floor(orderAmount * POINTS_PER_RUPEE);
        if (pointsToReverse <= 0) return;

        int before = user.getLoyaltyPoints();
        int after = Math.max(0, before - pointsToReverse); // never go below 0

        user.setLoyaltyPoints(after);
        userRepository.save(user);

        saveTransaction(user, TransactionType.REDEEMED, before - after, before, after,
                orderId, "Points reversed due to cancellation of order #" + orderId);
    }

    // ─── Customer endpoints ────────────────────────────────────────────────────

    /**
     * Get current loyalty balance for the logged-in customer.
     */
    public LoyaltyBalanceResponse getBalance(String email) {
        User user = findUserByEmail(email);
        return new LoyaltyBalanceResponse(user.getUserId(), user.getFullName(), user.getLoyaltyPoints());
    }

    /**
     * Redeem points for a discount (e.g. on an order).
     * Returns the rupee discount value applied.
     */
    @Transactional
    public double redeemPoints(String email, RedeemPointsRequest request) {
        User user = findUserByEmail(email);
        int currentPoints = user.getLoyaltyPoints();

        if (request.getPoints() > currentPoints) {
            throw new IllegalArgumentException(
                    "Insufficient points. Available: " + currentPoints
                            + ", Requested: " + request.getPoints());
        }

        int before = currentPoints;
        int after = before - request.getPoints();

        user.setLoyaltyPoints(after);
        userRepository.save(user);

        String desc = request.getOrderId() != null
                ? "Points redeemed for order #" + request.getOrderId()
                : "Points redeemed";

        saveTransaction(user, TransactionType.REDEEMED, request.getPoints(),
                before, after, request.getOrderId(), desc);

        return request.getPoints() * RUPEE_VALUE_PER_POINT;
    }

    /**
     * Customer views their own transaction history (paginated).
     */
    public Page<LoyaltyTransactionResponse> getMyHistory(String email, Pageable pageable) {
        User user = findUserByEmail(email);
        return transactionRepository
                .findByUserUserId(user.getUserId(), pageable)
                .map(LoyaltyTransactionResponse::from);
    }

    // ─── Admin endpoints ───────────────────────────────────────────────────────

    /**
     * Admin manually adjusts a user's points (positive = add, negative = deduct).
     */
    @Transactional
    public LoyaltyTransactionResponse adjustPoints(AdjustPointsRequest request) {
        User user = findUserById(request.getUserId());
        int before = user.getLoyaltyPoints();
        int after = before + request.getPoints();

        if (after < 0) {
            throw new IllegalArgumentException(
                    "Adjustment would result in negative balance. Current: " + before
                            + ", Adjustment: " + request.getPoints());
        }

        user.setLoyaltyPoints(after);
        userRepository.save(user);

        int absPoints = Math.abs(request.getPoints());
        TransactionType type = request.getPoints() >= 0
                ? TransactionType.EARNED
                : TransactionType.REDEEMED;

        String desc = request.getDescription() != null
                ? request.getDescription()
                : "Admin manual adjustment";

        LoyaltyTransaction tx = saveTransaction(user, TransactionType.ADJUSTED,
                absPoints, before, after, null, desc);

        return LoyaltyTransactionResponse.from(tx);
    }

    /**
     * Admin views a specific user's transaction history.
     */
    public Page<LoyaltyTransactionResponse> getUserHistory(String userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return transactionRepository
                .findByUserUserId(userId, pageable)
                .map(LoyaltyTransactionResponse::from);
    }

    /**
     * Admin gets balance for any user.
     */
    public LoyaltyBalanceResponse getBalanceForUser(String userId) {
        User user = findUserById(userId);
        return new LoyaltyBalanceResponse(user.getUserId(), user.getFullName(), user.getLoyaltyPoints());
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private LoyaltyTransaction saveTransaction(User user, TransactionType type, int points,
                                               int before, int after,
                                               String referenceId, String description) {
        LoyaltyTransaction tx = new LoyaltyTransaction();
        tx.setUser(user);
        tx.setType(type);
        tx.setPoints(points);
        tx.setBalanceBefore(before);
        tx.setBalanceAfter(after);
        tx.setReferenceId(referenceId);
        tx.setDescription(description);
        return transactionRepository.save(tx);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}