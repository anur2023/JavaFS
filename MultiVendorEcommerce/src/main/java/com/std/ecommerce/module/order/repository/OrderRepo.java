package com.std.ecommerce.module.order.repository;

import com.std.ecommerce.module.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    // All orders placed by a specific user
    List<Order> findByUserId(Long userId);

    // All orders for a specific status (e.g. PENDING)
    List<Order> findByStatus(String status);
}