package com.example.PharmacyOnlineStore.modules.inventory.service;

import com.example.PharmacyOnlineStore.common.exception.ResourceNotFoundException;
import com.example.PharmacyOnlineStore.modules.auth.entity.User;
import com.example.PharmacyOnlineStore.modules.auth.repository.UserRepository;
import com.example.PharmacyOnlineStore.modules.inventory.dto.AdjustStockRequest;
import com.example.PharmacyOnlineStore.modules.inventory.dto.InventoryLogResponse;
import com.example.PharmacyOnlineStore.modules.inventory.dto.StockSummaryResponse;
import com.example.PharmacyOnlineStore.modules.inventory.entity.ChangeType;
import com.example.PharmacyOnlineStore.modules.inventory.entity.InventoryLog;
import com.example.PharmacyOnlineStore.modules.inventory.repository.InventoryLogRepository;
import com.example.PharmacyOnlineStore.modules.medicine.entity.Medicine;
import com.example.PharmacyOnlineStore.modules.medicine.repository.MedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final MedicineRepository medicineRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final UserRepository userRepository;

    public InventoryService(MedicineRepository medicineRepository,
                            InventoryLogRepository inventoryLogRepository,
                            UserRepository userRepository) {
        this.medicineRepository = medicineRepository;
        this.inventoryLogRepository = inventoryLogRepository;
        this.userRepository = userRepository;
    }

    // ─── Internal methods (called by OrderService) ─────────────────────────────

    /**
     * Deducts stock when an order is confirmed.
     * Called by OrderService — no performer (system action).
     */
    @Transactional
    public void deductStockForOrder(String medicineId, int quantity, String orderId) {
        Medicine medicine = findMedicineById(medicineId);
        int before = medicine.getStockQuantity();
        int after = before - quantity;

        if (after < 0) {
            throw new IllegalStateException(
                    "Insufficient stock for medicine: " + medicine.getName()
                            + ". Available: " + before + ", Requested: " + quantity);
        }

        medicine.setStockQuantity(after);
        medicineRepository.save(medicine);

        saveLog(medicine, ChangeType.ORDER_DEDUCTION, -quantity, before, after, orderId, null, null);
    }

    /**
     * Restores stock when an order is cancelled.
     * Called by OrderService — no performer (system action).
     */
    @Transactional
    public void restoreStockForCancellation(String medicineId, int quantity, String orderId) {
        Medicine medicine = findMedicineById(medicineId);
        int before = medicine.getStockQuantity();
        int after = before + quantity;

        medicine.setStockQuantity(after);
        medicineRepository.save(medicine);

        saveLog(medicine, ChangeType.ORDER_CANCELLATION, quantity, before, after, orderId, null, null);
    }

    // ─── Admin / Pharmacist manual adjustment ──────────────────────────────────

    /**
     * Manual stock adjustment by an admin or pharmacist.
     */
    @Transactional
    public InventoryLogResponse adjustStock(String performerEmail, AdjustStockRequest request) {
        if (request.getChangeType() == ChangeType.ORDER_DEDUCTION
                || request.getChangeType() == ChangeType.ORDER_CANCELLATION) {
            throw new IllegalArgumentException(
                    "Manual adjustments must use MANUAL_RESTOCK or MANUAL_DEDUCTION only");
        }

        User performer = findUserByEmail(performerEmail);
        Medicine medicine = findMedicineById(request.getMedicineId());

        int before = medicine.getStockQuantity();
        int after = before + request.getQuantity(); // negative quantity = deduction

        if (after < 0) {
            throw new IllegalStateException(
                    "Adjustment would result in negative stock. Current: " + before
                            + ", Adjustment: " + request.getQuantity());
        }

        medicine.setStockQuantity(after);
        medicineRepository.save(medicine);

        InventoryLog log = saveLog(
                medicine,
                request.getChangeType(),
                request.getQuantity(),
                before,
                after,
                null,
                request.getNote(),
                performer
        );

        return InventoryLogResponse.from(log);
    }

    // ─── Queries ───────────────────────────────────────────────────────────────

    /**
     * All medicines with current stock levels (paginated).
     */
    public Page<StockSummaryResponse> getAllStockSummaries(Pageable pageable) {
        return medicineRepository.findAll(pageable)
                .map(StockSummaryResponse::from);
    }

    /**
     * Medicines with stock at or below threshold of 10.
     */
    public List<StockSummaryResponse> getLowStockMedicines() {
        return medicineRepository.findAll().stream()
                .filter(m -> m.getStockQuantity() <= 10)
                .map(StockSummaryResponse::from)
                .toList();
    }

    /**
     * Audit log for a specific medicine (paginated).
     */
    public Page<InventoryLogResponse> getLogsForMedicine(String medicineId, Pageable pageable) {
        return inventoryLogRepository
                .findByMedicineMedicineId(medicineId, pageable)
                .map(InventoryLogResponse::from);
    }

    /**
     * All inventory logs filtered by change type (paginated).
     */
    public Page<InventoryLogResponse> getLogsByChangeType(ChangeType changeType, Pageable pageable) {
        return inventoryLogRepository
                .findByChangeType(changeType, pageable)
                .map(InventoryLogResponse::from);
    }

    /**
     * All logs tied to a specific order.
     */
    public Page<InventoryLogResponse> getLogsByOrder(String orderId, Pageable pageable) {
        return inventoryLogRepository
                .findByReferenceId(orderId, pageable)
                .map(InventoryLogResponse::from);
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private InventoryLog saveLog(Medicine medicine, ChangeType changeType,
                                 int quantityChanged, int before, int after,
                                 String referenceId, String note, User performer) {
        InventoryLog log = new InventoryLog();
        log.setMedicine(medicine);
        log.setChangeType(changeType);
        log.setQuantityChanged(quantityChanged);
        log.setStockBefore(before);
        log.setStockAfter(after);
        log.setReferenceId(referenceId);
        log.setNote(note);
        log.setPerformedBy(performer);
        return inventoryLogRepository.save(log);
    }

    private Medicine findMedicineById(String id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + id));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}