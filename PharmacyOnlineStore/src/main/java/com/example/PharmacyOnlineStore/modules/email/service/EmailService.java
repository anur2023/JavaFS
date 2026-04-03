package com.example.PharmacyOnlineStore.modules.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOrderConfirmation(String toEmail, String fullName, String orderId,
                                      List<String> itemDetails, BigDecimal totalAmount,
                                      String deliveryAddress) {
        String subject = "Order Confirmed - #" + orderId.substring(0, 8).toUpperCase();
        String body = buildOrderConfirmationHtml(fullName, orderId, itemDetails, totalAmount, deliveryAddress);
        sendHtmlEmail(toEmail, subject, body);
    }

    @Async
    public void sendPrescriptionStatusEmail(String toEmail, String fullName, String orderId,
                                            String status, String reason) {
        String subject = "Prescription " + status + " - Order #" + orderId.substring(0, 8).toUpperCase();
        String body = buildPrescriptionStatusHtml(fullName, orderId, status, reason);
        sendHtmlEmail(toEmail, subject, body);
    }

    @Async
    public void sendPaymentConfirmation(String toEmail, String fullName, String orderId,
                                        String paymentId, BigDecimal amount) {
        String subject = "Payment Successful - Order #" + orderId.substring(0, 8).toUpperCase();
        String body = buildPaymentConfirmationHtml(fullName, orderId, paymentId, amount);
        sendHtmlEmail(toEmail, subject, body);
    }

    @Async
    public void sendPaymentFailure(String toEmail, String fullName, String orderId) {
        String subject = "Payment Failed - Order #" + orderId.substring(0, 8).toUpperCase();
        String body = buildPaymentFailureHtml(fullName, orderId);
        sendHtmlEmail(toEmail, subject, body);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to: " + to, e);
        }
    }

    private String buildOrderConfirmationHtml(String fullName, String orderId,
                                              List<String> itemDetails, BigDecimal totalAmount,
                                              String deliveryAddress) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        StringBuilder items = new StringBuilder();
        for (String item : itemDetails) {
            items.append("<li style='padding:4px 0;'>").append(item).append("</li>");
        }

        return """
                <html><body style='font-family:Arial,sans-serif;color:#333;'>
                  <div style='max-width:600px;margin:auto;border:1px solid #e0e0e0;border-radius:8px;overflow:hidden;'>
                    <div style='background:#2e7d32;padding:20px;text-align:center;'>
                      <h2 style='color:#fff;margin:0;'>Order Confirmed!</h2>
                    </div>
                    <div style='padding:24px;'>
                      <p>Hi <strong>%s</strong>,</p>
                      <p>Your order has been placed successfully.</p>
                      <table style='width:100%%;border-collapse:collapse;margin:16px 0;'>
                        <tr><td style='padding:6px 0;color:#666;'>Order ID</td>
                            <td style='padding:6px 0;font-weight:bold;'>#%s</td></tr>
                        <tr><td style='padding:6px 0;color:#666;'>Date</td>
                            <td style='padding:6px 0;'>%s</td></tr>
                        <tr><td style='padding:6px 0;color:#666;'>Delivery To</td>
                            <td style='padding:6px 0;'>%s</td></tr>
                      </table>
                      <p><strong>Items Ordered:</strong></p>
                      <ul style='margin:0;padding-left:20px;'>%s</ul>
                      <p style='font-size:18px;margin-top:16px;'>
                        Total: <strong style='color:#2e7d32;'>₹%s</strong>
                      </p>
                      <p style='color:#666;font-size:13px;'>
                        If your order contains prescription medicines, please ensure your prescription is uploaded and validated.
                      </p>
                    </div>
                    <div style='background:#f5f5f5;padding:12px;text-align:center;font-size:12px;color:#999;'>
                      PharmacyOnlineStore — Your Health, Our Priority
                    </div>
                  </div>
                </body></html>
                """.formatted(fullName, orderId.substring(0, 8).toUpperCase(),
                timestamp, deliveryAddress, items, totalAmount);
    }

    private String buildPrescriptionStatusHtml(String fullName, String orderId,
                                               String status, String reason) {
        String color = status.equalsIgnoreCase("APPROVED") ? "#2e7d32" : "#c62828";
        String reasonSection = (reason != null && !reason.isBlank())
                ? "<p><strong>Reason:</strong> " + reason + "</p>" : "";

        return """
                <html><body style='font-family:Arial,sans-serif;color:#333;'>
                  <div style='max-width:600px;margin:auto;border:1px solid #e0e0e0;border-radius:8px;overflow:hidden;'>
                    <div style='background:%s;padding:20px;text-align:center;'>
                      <h2 style='color:#fff;margin:0;'>Prescription %s</h2>
                    </div>
                    <div style='padding:24px;'>
                      <p>Hi <strong>%s</strong>,</p>
                      <p>Your prescription for Order <strong>#%s</strong> has been <strong>%s</strong>.</p>
                      %s
                      %s
                    </div>
                    <div style='background:#f5f5f5;padding:12px;text-align:center;font-size:12px;color:#999;'>
                      PharmacyOnlineStore — Your Health, Our Priority
                    </div>
                  </div>
                </body></html>
                """.formatted(color, status, fullName,
                orderId.substring(0, 8).toUpperCase(), status, reasonSection,
                status.equalsIgnoreCase("REJECTED")
                        ? "<p>Please upload a valid prescription to proceed with your order.</p>" : "");
    }

    private String buildPaymentConfirmationHtml(String fullName, String orderId,
                                                String paymentId, BigDecimal amount) {
        return """
                <html><body style='font-family:Arial,sans-serif;color:#333;'>
                  <div style='max-width:600px;margin:auto;border:1px solid #e0e0e0;border-radius:8px;overflow:hidden;'>
                    <div style='background:#1565c0;padding:20px;text-align:center;'>
                      <h2 style='color:#fff;margin:0;'>Payment Successful ✓</h2>
                    </div>
                    <div style='padding:24px;'>
                      <p>Hi <strong>%s</strong>,</p>
                      <p>Your payment has been received successfully.</p>
                      <table style='width:100%%;border-collapse:collapse;margin:16px 0;'>
                        <tr><td style='padding:6px 0;color:#666;'>Order ID</td>
                            <td style='padding:6px 0;font-weight:bold;'>#%s</td></tr>
                        <tr><td style='padding:6px 0;color:#666;'>Payment ID</td>
                            <td style='padding:6px 0;'>%s</td></tr>
                        <tr><td style='padding:6px 0;color:#666;'>Amount Paid</td>
                            <td style='padding:6px 0;font-weight:bold;color:#1565c0;'>₹%s</td></tr>
                      </table>
                      <p>Your order is now being processed.</p>
                    </div>
                    <div style='background:#f5f5f5;padding:12px;text-align:center;font-size:12px;color:#999;'>
                      PharmacyOnlineStore — Your Health, Our Priority
                    </div>
                  </div>
                </body></html>
                """.formatted(fullName, orderId.substring(0, 8).toUpperCase(), paymentId, amount);
    }

    private String buildPaymentFailureHtml(String fullName, String orderId) {
        return """
                <html><body style='font-family:Arial,sans-serif;color:#333;'>
                  <div style='max-width:600px;margin:auto;border:1px solid #e0e0e0;border-radius:8px;overflow:hidden;'>
                    <div style='background:#c62828;padding:20px;text-align:center;'>
                      <h2 style='color:#fff;margin:0;'>Payment Failed</h2>
                    </div>
                    <div style='padding:24px;'>
                      <p>Hi <strong>%s</strong>,</p>
                      <p>Unfortunately, your payment for Order <strong>#%s</strong> could not be processed.</p>
                      <p>Please try again or use a different payment method.</p>
                    </div>
                    <div style='background:#f5f5f5;padding:12px;text-align:center;font-size:12px;color:#999;'>
                      PharmacyOnlineStore — Your Health, Our Priority
                    </div>
                  </div>
                </body></html>
                """.formatted(fullName, orderId.substring(0, 8).toUpperCase());
    }
}