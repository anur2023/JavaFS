package com.example.PharmacyOnlineStore.modules.medicine.controller;

import com.example.PharmacyOnlineStore.modules.medicine.dto.MedicineRequest;
import com.example.PharmacyOnlineStore.modules.medicine.dto.MedicineResponse;
import com.example.PharmacyOnlineStore.modules.medicine.dto.StockUpdateRequest;
import com.example.PharmacyOnlineStore.modules.medicine.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<MedicineResponse> create(@Valid @RequestBody MedicineRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicineService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<MedicineResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String dosage,
            @RequestParam(required = false) String packaging,
            @RequestParam(required = false) Boolean requiresPrescription,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(medicineService.search(name, categoryId, dosage, packaging, requiresPrescription, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(medicineService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<MedicineResponse> update(@PathVariable String id,
                                                   @Valid @RequestBody MedicineRequest request) {
        return ResponseEntity.ok(medicineService.update(id, request));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<MedicineResponse> updateStock(@PathVariable String id,
                                                        @Valid @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(medicineService.updateStock(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}