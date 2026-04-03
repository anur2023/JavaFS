package com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository.UserRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.repository.ProductRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.dto.request.AddToWishlistRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.dto.response.WishlistResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.entity.Wishlist;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.entity.WishlistItem;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.repository.WishlistItemRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private Wishlist getOrCreateWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUserUserId(user.getUserId())
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    wishlist.setCreatedAt(LocalDateTime.now());
                    return wishlistRepository.save(wishlist);
                });
    }

    public WishlistResponse getWishlist(String email) {
        Wishlist wishlist = getOrCreateWishlist(email);
        List<WishlistResponse> itemResponses = wishlist.getItems() == null
                ? List.of()
                : wishlist.getItems().stream()
                .map(item -> new WishlistResponse(
                        item.getWishlistItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getProduct().getCategory(),
                        item.getAddedAt()
                )).collect(Collectors.toList());

        return new WishlistResponse(wishlist.getWishlistId(), itemResponses);
    }

    public String addToWishlist(String email, AddToWishlistRequest request) {
        Wishlist wishlist = getOrCreateWishlist(email);

        if (wishlistItemRepository.existsByWishlistWishlistIdAndProductProductId(
                wishlist.getWishlistId(), request.getProductId())) {
            throw new RuntimeException("Product already in wishlist");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);
        item.setAddedAt(LocalDateTime.now());
        wishlistItemRepository.save(item);

        return "Product added to wishlist";
    }

    public String removeFromWishlist(String email, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(email);

        WishlistItem item = wishlistItemRepository
                .findByWishlistWishlistIdAndProductProductId(wishlist.getWishlistId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in wishlist"));

        wishlistItemRepository.delete(item);
        return "Product removed from wishlist";
    }

    public String clearWishlist(String email) {
        Wishlist wishlist = getOrCreateWishlist(email);
        if (wishlist.getItems() != null) {
            wishlistItemRepository.deleteAll(wishlist.getItems());
        }
        return "Wishlist cleared";
    }
}