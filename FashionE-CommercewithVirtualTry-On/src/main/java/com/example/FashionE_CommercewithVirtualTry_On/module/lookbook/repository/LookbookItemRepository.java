package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.LookbookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookbookItemRepository extends JpaRepository<LookbookItem, Long> {

    boolean existsByLookbookLookbookIdAndProductProductId(Long lookbookId, Long productId);

    Optional<LookbookItem> findByLookbookLookbookIdAndProductProductId(Long lookbookId, Long productId);
}