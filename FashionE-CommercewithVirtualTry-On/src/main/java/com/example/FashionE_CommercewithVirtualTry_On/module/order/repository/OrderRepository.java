package com.example.FashionE_CommercewithVirtualTry_On.module.order.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUserIdOrderByOrderDateDesc(Long userId);
}