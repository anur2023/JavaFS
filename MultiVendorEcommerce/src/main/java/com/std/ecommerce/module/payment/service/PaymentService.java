package com.std.ecommerce.module.payment.service;

import com.std.ecommerce.module.order.entity.Order;
import com.std.ecommerce.module.order.repository.OrderRepo;
import com.std.ecommerce.module.payment.dto.PaymentRequest;
import com.std.ecommerce.module.payment.dto.PaymentResponse;
import com.std.ecommerce.module.payment.entity.Payment;
import com.std.ecommerce.module.payment.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrderRepo orderRepo;

    // 🔥 INITIATE payment for a confirmed order
    @Transactional
    public PaymentResponse initiatePayment(Long userId, PaymentRequest request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException(
                        "Order not found with id: " + request.getOrderId()));

        // Security: only the order owner can pay
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This order does not belong to you");
        }

        if (order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Cannot pay for a cancelled order");
        }

        // Prevent duplicate payment
        if (paymentRepo.findByOrderId(order.getId()).isPresent()) {
            throw new RuntimeException("Payment already exists for order id: " + order.getId());
        }

        // Simulate payment gateway — generate a fake transaction ID
        // In production: call Razorpay / Stripe / PayU SDK here
        String transactionId = "TXN-" + UUID.randomUUID().toString().toUpperCase().substring(0, 12);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId(transactionId);
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepo.save(payment);
        return toResponse(saved);
    }

    // 🔥 GET payment details for an order
    public PaymentResponse getPaymentByOrder(Long orderId, Long userId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This order does not belong to you");
        }

        Payment payment = paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "No payment found for order id: " + orderId));

        return toResponse(payment);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getPaidAt()
        );
    }
}