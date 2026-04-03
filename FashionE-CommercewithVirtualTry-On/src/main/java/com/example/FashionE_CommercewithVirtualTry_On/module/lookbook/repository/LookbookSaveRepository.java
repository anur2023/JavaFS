package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.LookbookSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LookbookSaveRepository extends JpaRepository<LookbookSave, Long> {

    List<LookbookSave> findByUserUserIdOrderBySavedAtDesc(Long userId);

    boolean existsByLookbookLookbookIdAndUserUserId(Long lookbookId, Long userId);

    Optional<LookbookSave> findByLookbookLookbookIdAndUserUserId(Long lookbookId, Long userId);

    // Count how many users saved a lookbook (popularity metric)
    long countByLookbookLookbookId(Long lookbookId);
}