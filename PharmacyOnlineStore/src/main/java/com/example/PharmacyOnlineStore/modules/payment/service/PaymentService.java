package com.example.PharmacyOnlineStore.modules.payment.service;

import com.example.PharmacyOnlineStore.common.exception.ConflictException;
import com.example.PharmacyOnlineStore.common.exception.ResourceNotFoundException;
import com.example.PharmacyOnlineStore.common.security.UserDetailsImpl;
import com.example.PharmacyOnlineStore.modules.email.service.EmailService;
import com.example.PharmacyOnlineStore.modules.payment.dto.PaymentRequest;
import com.example.PharmacyOnlineStore.modules.payment.dto.PaymentResponse;
import com.example.PharmacyOnlineStore.modules.payment.entity.Payment;
import com.example.PharmacyOnlineStore.modules.payment.entity.PaymentMethod;
import com.example.PharmacyOnlineStore.modules.payment.entity.PaymentStatus;
import com.example.PharmacyOnlineStore.modules.payment.repository.PaymentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EmailService emailService;

    public PaymentService(PaymentRepository paymentRepository, EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request, BigDecimal amount,
                                           String userEmail, String fullName) {
        if (paymentRepository.existsByOrderId(request.getOrderId())) {
            throw new ConflictException("Payment already initiated for this order");
        }

        String userId = getCurrentUserId();

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        payment = paymentRepository.save(payment);

        // Cash on delivery — mark success immediately
        if (request.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            return processSuccess(payment, userEmail, fullName);
        }

        // Simulate online payment — in production replace with Razorpay/Stripe SDK call
        boolean gatewaySuccess = simulateGateway(request.getPaymentMethod());

        if (gatewaySuccess) {
            return processSuccess(payment, userEmail, fullName);
        } else {
            return processFailure(payment, userEmail, fullName, "Payment declined by gateway");
        }
    }

    public PaymentResponse getByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
        return PaymentResponse.from(payment);
    }

    public PaymentResponse getByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return PaymentResponse.from(payment);
    }

    public List<PaymentResponse> getMyPayments() {
        String userId = getCurrentUserId();
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    // Admin only
    public List<PaymentResponse> getAllByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Transactional
    public PaymentResponse processRefund(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new ConflictException("Only successful payments can be refunded");
        }

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setTransactionReference("REFUND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    private PaymentResponse processSuccess(Payment payment, String userEmail, String fullName) {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Payment saved = paymentRepository.save(payment);

        emailService.sendPaymentConfirmation(
                userEmail, fullName, payment.getOrderId(),
                saved.getTransactionReference(), payment.getAmount()
        );

        return PaymentResponse.from(saved);
    }

    private PaymentResponse processFailure(Payment payment, String userEmail,
                                           String fullName, String reason) {
        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setFailureReason(reason);
        Payment saved = paymentRepository.save(payment);

        emailService.sendPaymentFailure(userEmail, fullName, payment.getOrderId());

        return PaymentResponse.from(saved);
    }

    // Simulates a payment gateway — replace with real SDK in production
    private boolean simulateGateway(PaymentMethod method) {
        // COD always succeeds, others simulate 90% success rate
        return Math.random() > 0.1;
    }

    private String getCurrentUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getUser().getUserId();
    }
}