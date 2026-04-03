package com.example.PharmacyOnlineStore.modules.prescription.controller;

import com.example.PharmacyOnlineStore.modules.prescription.dto.PrescriptionResponse;
import com.example.PharmacyOnlineStore.modules.prescription.dto.UploadPrescriptionRequest;
import com.example.PharmacyOnlineStore.modules.prescription.dto.ValidatePrescriptionRequest;
import com.example.PharmacyOnlineStore.modules.prescription.entity.ValidationStatus;
import com.example.PharmacyOnlineStore.modules.prescription.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // ─── CUSTOMER endpoints ────────────────────────────────────────────────────

    /**
     * POST /api/prescriptions
     * Customer uploads a prescription image URL for a prescription-required medicine.
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PrescriptionResponse> upload(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UploadPrescriptionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescriptionService.upload(userDetails.getUsername(), request));
    }

    /**
     * GET /api/prescriptions/my
     * Customer views their own prescription history (paginated).
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<PrescriptionResponse>> getMyPrescriptions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
        return ResponseEntity.ok(
                prescriptionService.getMyPrescriptions(userDetails.getUsername(), pageable));
    }

    /**
     * GET /api/prescriptions/my/{prescriptionId}
     * Customer views a specific prescription of theirs.
     */
    @GetMapping("/my/{prescriptionId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PrescriptionResponse> getMyPrescriptionById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String prescriptionId) {

        return ResponseEntity.ok(
                prescriptionService.getMyPrescriptionById(userDetails.getUsername(), prescriptionId));
    }

    // ─── PHARMACIST / ADMIN endpoints ─────────────────────────────────────────

    /**
     * GET /api/prescriptions/pending
     * Pharmacist/Admin fetches all PENDING prescriptions awaiting review.
     */
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    public ResponseEntity<Page<PrescriptionResponse>> getPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").ascending());
        return ResponseEntity.ok(prescriptionService.getPending(pageable));
    }

    /**
     * GET /api/prescriptions?status=PENDING|APPROVED|REJECTED
     * Pharmacist/Admin fetches prescriptions filtered by status.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    public ResponseEntity<Page<PrescriptionResponse>> getByStatus(
            @RequestParam(defaultValue = "PENDING") ValidationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
        return ResponseEntity.ok(prescriptionService.getByStatus(status, pageable));
    }

    /**
     * PATCH /api/prescriptions/{prescriptionId}/validate
     * Pharmacist/Admin approves or rejects a prescription.
     */
    @PatchMapping("/{prescriptionId}/validate")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    public ResponseEntity<PrescriptionResponse> validate(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String prescriptionId,
            @Valid @RequestBody ValidatePrescriptionRequest request) {

        return ResponseEntity.ok(
                prescriptionService.validate(userDetails.getUsername(), prescriptionId, request));
    }
}