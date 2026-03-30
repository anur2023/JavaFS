package com.std.ecommerce.module.cart.service;

import com.std.ecommerce.module.auth.entity.User;
import com.std.ecommerce.module.auth.repository.UserRepository;
import com.std.ecommerce.module.cart.dto.CartRequest;
import com.std.ecommerce.module.cart.dto.CartResponse;
import com.std.ecommerce.module.cart.dto.CartSummaryResponse;
import com.std.ecommerce.module.cart.entity.Cart;
import com.std.ecommerce.module.cart.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepository userRepository;

    // 🔥 ADD item to cart (or increase quantity if already exists)
    public CartResponse addToCart(Long userId, CartRequest request) {

        // 🔹 Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 🔹 Check if same product already in cart
        Optional<Cart> existingCart = cartRepo.findByUserIdAndProductId(userId, request.getProductId());

        Cart cart;

        if (existingCart.isPresent()) {
            // 🔥 Product already in cart → increase quantity
            cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        } else {
            // 🔥 New product → add to cart
            cart = new Cart();
            cart.setUser(user);
            cart.setProductId(request.getProductId());
            cart.setProductName(request.getProductName());
            cart.setProductPrice(request.getProductPrice());
            cart.setQuantity(request.getQuantity());
        }

        Cart saved = cartRepo.save(cart);
        return toResponse(saved);
    }

    // 🔥 GET cart summary for a user
    public CartSummaryResponse getCart(Long userId) {

        // 🔹 Validate user
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Cart> cartItems = cartRepo.findByUserId(userId);

        List<CartResponse> responseList = cartItems.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        // 🔹 Calculate grand total
        Double grandTotal = responseList.stream()
                .mapToDouble(CartResponse::getTotalPrice)
                .sum();

        return new CartSummaryResponse(responseList, responseList.size(), grandTotal);
    }

    // 🔥 UPDATE quantity of a cart item
    public CartResponse updateCartItem(Long userId, Long cartItemId, Integer quantity) {

        Cart cart = cartRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        // 🔹 Security check: ensure cart item belongs to this user
        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This cart item does not belong to you");
        }

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be at least 1");
        }

        cart.setQuantity(quantity);
        Cart updated = cartRepo.save(cart);
        return toResponse(updated);
    }

    // 🔥 REMOVE single item from cart
    public String removeCartItem(Long userId, Long cartItemId) {

        Cart cart = cartRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        // 🔹 Security check
        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This cart item does not belong to you");
        }

        cartRepo.delete(cart);
        return "Item removed from cart";
    }

    // 🔥 CLEAR entire cart for a user
    @Transactional
    public String clearCart(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        cartRepo.deleteByUserId(userId);
        return "Cart cleared successfully";
    }

    // 🔹 Helper: Entity → Response DTO
    private CartResponse toResponse(Cart cart) {
        Double totalPrice = cart.getProductPrice() * cart.getQuantity();
        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                cart.getProductId(),
                cart.getProductName(),
                cart.getProductPrice(),
                cart.getQuantity(),
                totalPrice
        );
    }
}