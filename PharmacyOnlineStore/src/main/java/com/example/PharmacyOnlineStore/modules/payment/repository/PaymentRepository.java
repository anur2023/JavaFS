package com.example.PharmacyOnlineStore.modules.payment.repository;
import com.example.PharmacyOnlineStore.modules.payment.entity.Payment;
import com.example.PharmacyOnlineStore.modules.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByUserId(String userId);
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    boolean existsByOrderId(String orderId);
}