package com.example.PharmacyOnlineStore.modules.inventory.repository;

import com.example.PharmacyOnlineStore.modules.inventory.entity.ChangeType;
import com.example.PharmacyOnlineStore.modules.inventory.entity.InventoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, String> {

    // Logs for a specific medicine
    Page<InventoryLog> findByMedicineMedicineId(String medicineId, Pageable pageable);

    // Logs by change type (e.g. all ORDER_DEDUCTION)
    Page<InventoryLog> findByChangeType(ChangeType changeType, Pageable pageable);

    // Logs for a specific order (referenceId = orderId)
    Page<InventoryLog> findByReferenceId(String referenceId, Pageable pageable);
}