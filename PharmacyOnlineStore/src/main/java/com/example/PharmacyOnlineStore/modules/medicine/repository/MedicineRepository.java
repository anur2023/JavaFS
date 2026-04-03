package com.example.PharmacyOnlineStore.modules.medicine.repository;

import com.example.PharmacyOnlineStore.modules.medicine.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicineRepository extends JpaRepository<Medicine, String> {

    Page<Medicine> findByCategoryCategoryId(String categoryId, Pageable pageable);

    Page<Medicine> findByDosageIgnoreCase(String dosage, Pageable pageable);

    Page<Medicine> findByPackagingIgnoreCase(String packaging, Pageable pageable);

    @Query("""
            SELECT m FROM Medicine m
            WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:categoryId IS NULL OR m.category.categoryId = :categoryId)
            AND (:dosage IS NULL OR LOWER(m.dosage) = LOWER(:dosage))
            AND (:packaging IS NULL OR LOWER(m.packaging) = LOWER(:packaging))
            AND (:requiresPrescription IS NULL OR m.requiresPrescription = :requiresPrescription)
            """)
    Page<Medicine> searchMedicines(
            @Param("name") String name,
            @Param("categoryId") String categoryId,
            @Param("dosage") String dosage,
            @Param("packaging") String packaging,
            @Param("requiresPrescription") Boolean requiresPrescription,
            Pageable pageable
    );

    boolean existsByNameIgnoreCaseAndCategoryCategoryId(String name, String categoryId);
}