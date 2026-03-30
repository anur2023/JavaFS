package com.std.ecommerce.module.order.repository;

import com.std.ecommerce.module.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

    // All items in a specific order
    List<OrderItem> findByOrderId(Long orderId);

    // All items sold by a specific vendor (across all orders)
    List<OrderItem> findByVendorId(Long vendorId);
}