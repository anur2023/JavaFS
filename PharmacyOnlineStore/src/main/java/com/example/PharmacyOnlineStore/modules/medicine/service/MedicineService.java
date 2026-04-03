package com.example.PharmacyOnlineStore.modules.medicine.service;

import com.example.PharmacyOnlineStore.common.exception.ConflictException;
import com.example.PharmacyOnlineStore.common.exception.ResourceNotFoundException;
import com.example.PharmacyOnlineStore.modules.medicine.dto.MedicineRequest;
import com.example.PharmacyOnlineStore.modules.medicine.dto.MedicineResponse;
import com.example.PharmacyOnlineStore.modules.medicine.dto.StockUpdateRequest;
import com.example.PharmacyOnlineStore.modules.medicine.entity.Category;
import com.example.PharmacyOnlineStore.modules.medicine.entity.Medicine;
import com.example.PharmacyOnlineStore.modules.medicine.repository.MedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final CategoryService categoryService;

    public MedicineService(MedicineRepository medicineRepository, CategoryService categoryService) {
        this.medicineRepository = medicineRepository;
        this.categoryService = categoryService;
    }

    public MedicineResponse create(MedicineRequest request) {
        Category category = categoryService.findById(request.getCategoryId());

        if (medicineRepository.existsByNameIgnoreCaseAndCategoryCategoryId(request.getName(), request.getCategoryId())) {
            throw new ConflictException("Medicine already exists in this category");
        }

        Medicine medicine = buildMedicine(new Medicine(), request, category);
        return MedicineResponse.from(medicineRepository.save(medicine));
    }

    public Page<MedicineResponse> search(String name, String categoryId, String dosage,
                                         String packaging, Boolean requiresPrescription, Pageable pageable) {
        return medicineRepository.searchMedicines(name, categoryId, dosage, packaging, requiresPrescription, pageable)
                .map(MedicineResponse::from);
    }

    public MedicineResponse getById(String id) {
        return MedicineResponse.from(findById(id));
    }

    public MedicineResponse update(String id, MedicineRequest request) {
        Medicine medicine = findById(id);
        Category category = categoryService.findById(request.getCategoryId());

        medicineRepository.findById(id).ifPresent(existing -> {
            boolean nameChanged = !existing.getName().equalsIgnoreCase(request.getName());
            boolean categoryChanged = !existing.getCategory().getCategoryId().equals(request.getCategoryId());
            if ((nameChanged || categoryChanged) &&
                    medicineRepository.existsByNameIgnoreCaseAndCategoryCategoryId(request.getName(), request.getCategoryId())) {
                throw new ConflictException("Medicine already exists in this category");
            }
        });

        buildMedicine(medicine, request, category);
        return MedicineResponse.from(medicineRepository.save(medicine));
    }

    @Transactional
    public MedicineResponse updateStock(String id, StockUpdateRequest request) {
        Medicine medicine = findById(id);
        medicine.setStockQuantity(request.getStockQuantity());
        return MedicineResponse.from(medicineRepository.save(medicine));
    }

    public void delete(String id) {
        medicineRepository.delete(findById(id));
    }

    // Called internally by order module when order is placed
    @Transactional
    public void decreaseStock(String medicineId, int quantity) {
        Medicine medicine = findById(medicineId);
        int updated = medicine.getStockQuantity() - quantity;
        if (updated < 0) {
            throw new IllegalStateException("Insufficient stock for medicine: " + medicine.getName());
        }
        medicine.setStockQuantity(updated);
        medicineRepository.save(medicine);
    }

    public Medicine findById(String id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
    }

    private Medicine buildMedicine(Medicine medicine, MedicineRequest request, Category category) {
        medicine.setName(request.getName());
        medicine.setCategory(category);
        medicine.setDosage(request.getDosage());
        medicine.setPackaging(request.getPackaging());
        medicine.setPrice(request.getPrice());
        medicine.setRequiresPrescription(request.isRequiresPrescription());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setDescription(request.getDescription());
        return medicine;
    }
}