package com.example.PharmacyOnlineStore.modules.order.repository;

import com.example.PharmacyOnlineStore.modules.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderOrderId(Long orderId);

    List<OrderItem> findByMedicineId(Long medicineId);
}