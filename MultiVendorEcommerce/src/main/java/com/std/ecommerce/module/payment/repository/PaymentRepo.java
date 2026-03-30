package com.std.ecommerce.module.payment.repository;

import com.std.ecommerce.module.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    // Get payment linked to a specific order
    Optional<Payment> findByOrderId(Long orderId);
}