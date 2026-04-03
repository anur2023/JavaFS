package com.example.PharmacyOnlineStore.modules.prescription.service;

import com.example.PharmacyOnlineStore.common.exception.ResourceNotFoundException;
import com.example.PharmacyOnlineStore.common.security.UserDetailsImpl;
import com.example.PharmacyOnlineStore.modules.auth.entity.User;
import com.example.PharmacyOnlineStore.modules.auth.repository.UserRepository;
import com.example.PharmacyOnlineStore.modules.medicine.entity.Medicine;
import com.example.PharmacyOnlineStore.modules.medicine.service.MedicineService;
import com.example.PharmacyOnlineStore.modules.prescription.dto.PrescriptionResponse;
import com.example.PharmacyOnlineStore.modules.prescription.dto.UploadPrescriptionRequest;
import com.example.PharmacyOnlineStore.modules.prescription.dto.ValidatePrescriptionRequest;
import com.example.PharmacyOnlineStore.modules.prescription.entity.Prescription;
import com.example.PharmacyOnlineStore.modules.prescription.entity.ValidationStatus;
import com.example.PharmacyOnlineStore.modules.prescription.repository.PrescriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicineService medicineService;
    private final UserRepository userRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               MedicineService medicineService,
                               UserRepository userRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicineService = medicineService;
        this.userRepository = userRepository;
    }

    // CUSTOMER: upload a prescription for a prescription-required medicine
    @Transactional
    public PrescriptionResponse upload(String customerEmail, UploadPrescriptionRequest request) {
        User customer = findUserByEmail(customerEmail);
        Medicine medicine = medicineService.findById(request.getMedicineId());

        if (!medicine.isRequiresPrescription()) {
            throw new IllegalArgumentException("This medicine does not require a prescription");
        }

        Prescription prescription = new Prescription();
        prescription.setCustomer(customer);
        prescription.setMedicine(medicine);
        prescription.setImageUrl(request.getImageUrl());
        prescription.setStatus(ValidationStatus.PENDING);

        return PrescriptionResponse.from(prescriptionRepository.save(prescription));
    }

    // CUSTOMER: view their own prescriptions (paginated)
    public Page<PrescriptionResponse> getMyPrescriptions(String customerEmail, Pageable pageable) {
        User customer = findUserByEmail(customerEmail);
        return prescriptionRepository
                .findByCustomerUserId(customer.getUserId(), pageable)
                .map(PrescriptionResponse::from);
    }

    // CUSTOMER: view a specific prescription (only their own)
    public PrescriptionResponse getMyPrescriptionById(String customerEmail, String prescriptionId) {
        User customer = findUserByEmail(customerEmail);
        Prescription prescription = findById(prescriptionId);

        if (!prescription.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new AccessDeniedException("You do not have access to this prescription");
        }
        return PrescriptionResponse.from(prescription);
    }

    // PHARMACIST/ADMIN: get all pending prescriptions
    public Page<PrescriptionResponse> getPending(Pageable pageable) {
        return prescriptionRepository
                .findByStatus(ValidationStatus.PENDING, pageable)
                .map(PrescriptionResponse::from);
    }

    // PHARMACIST/ADMIN: get prescriptions by status
    public Page<PrescriptionResponse> getByStatus(ValidationStatus status, Pageable pageable) {
        return prescriptionRepository
                .findByStatus(status, pageable)
                .map(PrescriptionResponse::from);
    }

    // PHARMACIST/ADMIN: validate (approve or reject) a prescription
    @Transactional
    public PrescriptionResponse validate(String reviewerEmail, String prescriptionId,
                                         ValidatePrescriptionRequest request) {
        User reviewer = findUserByEmail(reviewerEmail);
        Prescription prescription = findById(prescriptionId);

        if (prescription.getStatus() != ValidationStatus.PENDING) {
            throw new IllegalStateException("Prescription has already been reviewed");
        }

        if (request.getStatus() == ValidationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot set status back to PENDING");
        }

        prescription.setStatus(request.getStatus());
        prescription.setPharmacistNote(request.getPharmacistNote());
        prescription.setReviewedBy(reviewer);
        prescription.setReviewedAt(LocalDateTime.now());

        return PrescriptionResponse.from(prescriptionRepository.save(prescription));
    }

    // Utility: used by order module to check if a customer has an approved prescription for a medicine
    public boolean hasApprovedPrescription(String customerId, String medicineId) {
        return prescriptionRepository
                .findByCustomerUserIdAndMedicineMedicineIdAndStatus(customerId, medicineId, ValidationStatus.APPROVED)
                .isPresent();
    }

    public Prescription findById(String id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}