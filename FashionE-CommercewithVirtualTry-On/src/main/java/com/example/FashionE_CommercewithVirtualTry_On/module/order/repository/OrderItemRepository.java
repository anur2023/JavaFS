package com.example.FashionE_CommercewithVirtualTry_On.module.order.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}