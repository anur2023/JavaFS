package com.example.PharmacyOnlineStore.modules.inventory.controller;

import com.example.PharmacyOnlineStore.modules.inventory.dto.AdjustStockRequest;
import com.example.PharmacyOnlineStore.modules.inventory.dto.InventoryLogResponse;
import com.example.PharmacyOnlineStore.modules.inventory.dto.StockSummaryResponse;
import com.example.PharmacyOnlineStore.modules.inventory.entity.ChangeType;
import com.example.PharmacyOnlineStore.modules.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * GET /api/inventory/stock
     * All medicines with current stock levels (paginated).
     */
    @GetMapping("/stock")
    public ResponseEntity<Page<StockSummaryResponse>> getAllStock(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ResponseEntity.ok(inventoryService.getAllStockSummaries(pageable));
    }

    /**
     * GET /api/inventory/stock/low
     * Medicines with stock <= 10 units.
     */
    @GetMapping("/stock/low")
    public ResponseEntity<List<StockSummaryResponse>> getLowStock() {
        return ResponseEntity.ok(inventoryService.getLowStockMedicines());
    }

    /**
     * POST /api/inventory/adjust
     * Manually restock or deduct stock for a medicine.
     * Body: { medicineId, quantity (positive/negative), changeType (MANUAL_RESTOCK/MANUAL_DEDUCTION), note }
     */
    @PostMapping("/adjust")
    public ResponseEntity<InventoryLogResponse> adjustStock(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AdjustStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.adjustStock(userDetails.getUsername(), request));
    }

    /**
     * GET /api/inventory/logs?medicineId={id}
     * Audit log for a specific medicine.
     */
    @GetMapping("/logs")
    public ResponseEntity<Page<InventoryLogResponse>> getLogsForMedicine(
            @RequestParam String medicineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(inventoryService.getLogsForMedicine(medicineId, pageable));
    }

    /**
     * GET /api/inventory/logs/type?changeType=ORDER_DEDUCTION
     * All logs filtered by change type.
     */
    @GetMapping("/logs/type")
    public ResponseEntity<Page<InventoryLogResponse>> getLogsByType(
            @RequestParam ChangeType changeType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(inventoryService.getLogsByChangeType(changeType, pageable));
    }

    /**
     * GET /api/inventory/logs/order/{orderId}
     * All inventory changes tied to a specific order.
     */
    @GetMapping("/logs/order/{orderId}")
    public ResponseEntity<Page<InventoryLogResponse>> getLogsByOrder(
            @PathVariable String orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(inventoryService.getLogsByOrder(orderId, pageable));
    }
}