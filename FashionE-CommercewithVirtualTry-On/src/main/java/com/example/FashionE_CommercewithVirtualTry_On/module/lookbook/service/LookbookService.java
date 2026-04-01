package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository.UserRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request.AddLookbookItemRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request.CreateLookbookRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.request.UpdateLookbookRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.response.LookbookItemResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.dto.response.LookbookResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.Lookbook;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.LookbookItem;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.LookbookSave;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.LookbookStatus;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.repository.LookbookItemRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.repository.LookbookRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.repository.LookbookSaveRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.ProductImage;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LookbookService {

    @Autowired
    private LookbookRepository lookbookRepository;

    @Autowired
    private LookbookItemRepository lookbookItemRepository;

    @Autowired
    private LookbookSaveRepository lookbookSaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // ─── Admin: Create Lookbook ───────────────────────────────────────────────

    @Transactional
    public LookbookResponse createLookbook(String adminEmail, CreateLookbookRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Lookbook title is required");
        }

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        Lookbook lookbook = new Lookbook();
        lookbook.setTitle(request.getTitle());
        lookbook.setDescription(request.getDescription());
        lookbook.setSeason(request.getSeason());
        lookbook.setStyle(request.getStyle());
        lookbook.setCoverImageUrl(request.getCoverImageUrl());
        lookbook.setStatus(LookbookStatus.DRAFT);   // always starts as DRAFT
        lookbook.setCreatedBy(admin);
        lookbook.setCreatedAt(LocalDateTime.now());

        Lookbook saved = lookbookRepository.save(lookbook);

        // Optionally attach products at creation time
        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            List<LookbookItem> items = new ArrayList<>();
            int order = 0;
            for (Long productId : request.getProductIds()) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

                LookbookItem item = new LookbookItem();
                item.setLookbook(saved);
                item.setProduct(product);
                item.setDisplayOrder(order++);
                items.add(lookbookItemRepository.save(item));
            }
            saved.setItems(items);
        }

        return mapToResponse(saved, null);
    }

    // ─── Admin: Update Lookbook ───────────────────────────────────────────────

    @Transactional
    public LookbookResponse updateLookbook(Long lookbookId, UpdateLookbookRequest request) {

        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new RuntimeException("Lookbook not found: " + lookbookId));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            lookbook.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            lookbook.setDescription(request.getDescription());
        }
        if (request.getSeason() != null) {
            lookbook.setSeason(request.getSeason());
        }
        if (request.getStyle() != null) {
            lookbook.setStyle(request.getStyle());
        }
        if (request.getCoverImageUrl() != null) {
            lookbook.setCoverImageUrl(request.getCoverImageUrl());
        }
        if (request.getStatus() != null) {
            lookbook.setStatus(LookbookStatus.valueOf(request.getStatus().toUpperCase()));
        }
        lookbook.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(lookbookRepository.save(lookbook), null);
    }

    // ─── Admin: Delete Lookbook ───────────────────────────────────────────────

    @Transactional
    public String deleteLookbook(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new RuntimeException("Lookbook not found: " + lookbookId));

        lookbookRepository.delete(lookbook);
        return "Lookbook deleted successfully";
    }

    // ─── Admin: Add product to lookbook ──────────────────────────────────────

    @Transactional
    public LookbookResponse addItemToLookbook(Long lookbookId, AddLookbookItemRequest request) {

        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new RuntimeException("Lookbook not found: " + lookbookId));

        if (lookbookItemRepository.existsByLookbookLookbookIdAndProductProductId(
                lookbookId, request.getProductId())) {
            throw new RuntimeException("Product already added to this lookbook");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        LookbookItem item = new LookbookItem();
        item.setLookbook(lookbook);
        item.setProduct(product);
        item.setStylingNote(request.getStylingNote());
        item.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        lookbookItemRepository.save(item);

        // Refresh lookbook to get updated items list
        Lookbook refreshed = lookbookRepository.findById(lookbookId).get();
        return mapToResponse(refreshed, null);
    }

    // ─── Admin: Remove product from lookbook ─────────────────────────────────

    @Transactional
    public String removeItemFromLookbook(Long lookbookId, Long productId) {

        LookbookItem item = lookbookItemRepository
                .findByLookbookLookbookIdAndProductProductId(lookbookId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found in lookbook"));

        lookbookItemRepository.delete(item);
        return "Product removed from lookbook";
    }

    // ─── Admin: Get all lookbooks ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LookbookResponse> getAllLookbooksAdmin() {
        return lookbookRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(lb -> mapToResponse(lb, null))
                .collect(Collectors.toList());
    }

    // ─── Customer: Get all PUBLISHED lookbooks ────────────────────────────────

    @Transactional(readOnly = true)
    public List<LookbookResponse> getPublishedLookbooks(String email,
                                                        String season,
                                                        String style,
                                                        String search) {
        List<Lookbook> lookbooks;

        if (search != null && !search.isBlank()) {
            lookbooks = lookbookRepository
                    .findByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                            LookbookStatus.PUBLISHED, search);
        } else if (season != null && !season.isBlank()) {
            lookbooks = lookbookRepository
                    .findByStatusAndSeasonIgnoreCaseOrderByCreatedAtDesc(
                            LookbookStatus.PUBLISHED, season);
        } else if (style != null && !style.isBlank()) {
            lookbooks = lookbookRepository
                    .findByStatusAndStyleIgnoreCaseOrderByCreatedAtDesc(
                            LookbookStatus.PUBLISHED, style);
        } else {
            lookbooks = lookbookRepository
                    .findByStatusOrderByCreatedAtDesc(LookbookStatus.PUBLISHED);
        }

        Long userId = resolveUserId(email);
        return lookbooks.stream()
                .map(lb -> mapToResponse(lb, userId))
                .collect(Collectors.toList());
    }

    // ─── Customer: Get single PUBLISHED lookbook ──────────────────────────────

    @Transactional(readOnly = true)
    public LookbookResponse getPublishedLookbookById(String email, Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new RuntimeException("Lookbook not found: " + lookbookId));

        if (lookbook.getStatus() != LookbookStatus.PUBLISHED) {
            throw new RuntimeException("Lookbook is not available");
        }

        Long userId = resolveUserId(email);
        return mapToResponse(lookbook, userId);
    }

    // ─── Customer: Save / bookmark a lookbook ────────────────────────────────

    @Transactional
    public String saveLookbook(String email, Long lookbookId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new RuntimeException("Lookbook not found: " + lookbookId));

        if (lookbook.getStatus() != LookbookStatus.PUBLISHED) {
            throw new RuntimeException("Lookbook is not available");
        }

        if (lookbookSaveRepository.existsByLookbookLookbookIdAndUserUserId(
                lookbookId, user.getUserId())) {
            throw new RuntimeException("Lookbook already saved");
        }

        LookbookSave save = new LookbookSave();
        save.setLookbook(lookbook);
        save.setUser(user);
        save.setSavedAt(LocalDateTime.now());
        lookbookSaveRepository.save(save);

        return "Lookbook saved successfully";
    }

    // ─── Customer: Unsave a lookbook ─────────────────────────────────────────

    @Transactional
    public String unsaveLookbook(String email, Long lookbookId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LookbookSave save = lookbookSaveRepository
                .findByLookbookLookbookIdAndUserUserId(lookbookId, user.getUserId())
                .orElseThrow(() -> new RuntimeException("Lookbook not saved by user"));

        lookbookSaveRepository.delete(save);
        return "Lookbook removed from saved list";
    }

    // ─── Customer: Get all saved lookbooks ───────────────────────────────────

    @Transactional(readOnly = true)
    public List<LookbookResponse> getMySavedLookbooks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return lookbookSaveRepository
                .findByUserUserIdOrderBySavedAtDesc(user.getUserId())
                .stream()
                .map(save -> mapToResponse(save.getLookbook(), user.getUserId()))
                .collect(Collectors.toList());
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Long resolveUserId(String email) {
        if (email == null) return null;
        return userRepository.findByEmail(email)
                .map(u -> u.getUserId())
                .orElse(null);
    }

    private String resolvePrimaryImageUrl(Product product) {
        if (product.getImages() == null || product.getImages().isEmpty()) return null;
        return product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(product.getImages().get(0).getImageUrl());
    }

    private LookbookItemResponse mapItemToResponse(LookbookItem item) {
        LookbookItemResponse res = new LookbookItemResponse();
        res.setLookbookItemId(item.getLookbookItemId());
        res.setProductId(item.getProduct().getProductId());
        res.setProductName(item.getProduct().getName());
        res.setProductCategory(item.getProduct().getCategory());
        res.setProductPrice(item.getProduct().getPrice());
        res.setProductImageUrl(resolvePrimaryImageUrl(item.getProduct()));
        res.setStylingNote(item.getStylingNote());
        res.setDisplayOrder(item.getDisplayOrder());
        return res;
    }

    private LookbookResponse mapToResponse(Lookbook lookbook, Long currentUserId) {
        LookbookResponse res = new LookbookResponse();
        res.setLookbookId(lookbook.getLookbookId());
        res.setTitle(lookbook.getTitle());
        res.setDescription(lookbook.getDescription());
        res.setSeason(lookbook.getSeason());
        res.setStyle(lookbook.getStyle());
        res.setCoverImageUrl(lookbook.getCoverImageUrl());
        res.setStatus(lookbook.getStatus().name());
        res.setCreatedByEmail(lookbook.getCreatedBy().getEmail());
        res.setCreatedAt(lookbook.getCreatedAt());
        res.setUpdatedAt(lookbook.getUpdatedAt());

        // Map items sorted by displayOrder
        if (lookbook.getItems() != null) {
            List<LookbookItemResponse> itemResponses = lookbook.getItems().stream()
                    .sorted((a, b) -> Integer.compare(a.getDisplayOrder(), b.getDisplayOrder()))
                    .map(this::mapItemToResponse)
                    .collect(Collectors.toList());
            res.setItems(itemResponses);
        }

        // Save count & savedByMe flag
        res.setSavedCount(lookbookSaveRepository.countByLookbookLookbookId(lookbook.getLookbookId()));
        if (currentUserId != null) {
            res.setSavedByMe(lookbookSaveRepository.existsByLookbookLookbookIdAndUserUserId(
                    lookbook.getLookbookId(), currentUserId));
        }

        return res;
    }
}