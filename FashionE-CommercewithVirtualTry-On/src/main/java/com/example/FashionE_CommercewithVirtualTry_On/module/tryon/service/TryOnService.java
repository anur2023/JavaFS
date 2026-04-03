package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository.UserRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.ProductImage;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.repository.ProductRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.client.ArApiClient;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.dto.request.TryOnRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.dto.response.TryOnResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.entity.TryOnSession;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.entity.TryOnStatus;
import com.example.FashionE_CommercewithVirtualTry_On.module.tryon.repository.TryOnSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TryOnService {

    @Autowired
    private TryOnSessionRepository tryOnSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArApiClient arApiClient;

    // ─── Start a try-on session ───────────────────────────────────────────────

    @Transactional
    public TryOnResponse startTryOn(String email, TryOnRequest request) {

        if (request.getUserImageUrl() == null || request.getUserImageUrl().isBlank()) {
            throw new RuntimeException("User image URL is required");
        }
        if (request.getProductId() == null) {
            throw new RuntimeException("Product ID is required");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        // Create session in PENDING state
        TryOnSession session = new TryOnSession();
        session.setUser(user);
        session.setProduct(product);
        session.setUserImageUrl(request.getUserImageUrl());
        session.setStatus(TryOnStatus.PENDING);
        session.setCreatedAt(LocalDateTime.now());

        // Resolve the product's first image to send to the AR API
        String productImageUrl = resolveProductImageUrl(product);

        // Call external AR service
        try {
            String resultUrl = arApiClient.performTryOn(request.getUserImageUrl(), productImageUrl);
            session.setResultImageUrl(resultUrl);
            session.setStatus(TryOnStatus.COMPLETED);
            session.setApiMessage("Try-on completed successfully");
        } catch (Exception e) {
            session.setStatus(TryOnStatus.FAILED);
            session.setApiMessage("AR service error: " + e.getMessage());
        }

        TryOnSession saved = tryOnSessionRepository.save(session);
        return mapToResponse(saved);
    }

    // ─── Get all sessions for logged-in user ─────────────────────────────────

    @Transactional(readOnly = true)
    public List<TryOnResponse> getMyTryOnHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tryOnSessionRepository
                .findByUserUserIdOrderByCreatedAtDesc(user.getUserId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── Get a single session by ID (scoped to user) ─────────────────────────

    @Transactional(readOnly = true)
    public TryOnResponse getSessionById(String email, Long sessionId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TryOnSession session = tryOnSessionRepository
                .findBySessionIdAndUserUserId(sessionId, user.getUserId())
                .orElseThrow(() -> new RuntimeException("Try-on session not found: " + sessionId));

        return mapToResponse(session);
    }

    // ─── Delete a session (user removes from history) ────────────────────────

    @Transactional
    public String deleteSession(String email, Long sessionId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TryOnSession session = tryOnSessionRepository
                .findBySessionIdAndUserUserId(sessionId, user.getUserId())
                .orElseThrow(() -> new RuntimeException("Try-on session not found: " + sessionId));

        tryOnSessionRepository.delete(session);
        return "Try-on session deleted successfully";
    }

    // ─── Admin: get all sessions ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TryOnResponse> getAllSessions() {
        return tryOnSessionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── Admin: get sessions by product ──────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TryOnResponse> getSessionsByProduct(Long productId) {
        return tryOnSessionRepository.findByProductProductId(productId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── Helper: resolve product image URL ───────────────────────────────────

    private String resolveProductImageUrl(Product product) {
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            // Prefer the primary image if one is flagged
            return product.getImages().stream()
                    .filter(ProductImage::isPrimary)
                    .map(ProductImage::getImageUrl)
                    .findFirst()
                    .orElse(product.getImages().get(0).getImageUrl());
        }
        return null; // ArApiClient will handle null as an error
    }

    // ─── Mapper ──────────────────────────────────────────────────────────────

    private TryOnResponse mapToResponse(TryOnSession session) {
        TryOnResponse res = new TryOnResponse();
        res.setSessionId(session.getSessionId());
        res.setUserId(session.getUser().getUserId());
        res.setUserEmail(session.getUser().getEmail());
        res.setProductId(session.getProduct().getProductId());
        res.setProductName(session.getProduct().getName());
        res.setProductCategory(session.getProduct().getCategory());
        res.setUserImageUrl(session.getUserImageUrl());
        res.setResultImageUrl(session.getResultImageUrl());
        res.setStatus(session.getStatus().name());
        res.setApiMessage(session.getApiMessage());
        res.setCreatedAt(session.getCreatedAt());
        return res;
    }
}