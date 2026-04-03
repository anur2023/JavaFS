package com.example.PharmacyOnlineStore.modules.prescription.repository;

import com.example.PharmacyOnlineStore.modules.prescription.entity.Prescription;
import com.example.PharmacyOnlineStore.modules.prescription.entity.ValidationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, String> {

    // Customer: view their own prescriptions
    Page<Prescription> findByCustomerUserId(String customerId, Pageable pageable);

    // Customer: check if they already have an approved prescription for a medicine
    Optional<Prescription> findByCustomerUserIdAndMedicineMedicineIdAndStatus(
            String customerId, String medicineId, ValidationStatus status);

    // Pharmacist/Admin: view pending prescriptions
    Page<Prescription> findByStatus(ValidationStatus status, Pageable pageable);

    // All prescriptions for a specific medicine (admin use)
    List<Prescription> findByMedicineMedicineId(String medicineId);
}