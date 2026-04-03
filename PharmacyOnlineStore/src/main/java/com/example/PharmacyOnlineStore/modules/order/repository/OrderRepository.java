package com.example.PharmacyOnlineStore.modules.order.repository;

import com.example.PharmacyOnlineStore.modules.order.entity.Order;
import com.example.PharmacyOnlineStore.modules.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // All orders for a specific customer
    List<Order> findByCustomerId(Long customerId);

    // All orders for a customer filtered by status
    List<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus status);

    // All orders by status (used by admin/pharmacist)
    List<Order> findByOrderStatus(OrderStatus status);
}