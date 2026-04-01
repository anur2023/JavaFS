package com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.Lookbook;
import com.example.FashionE_CommercewithVirtualTry_On.module.lookbook.entity.LookbookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookbookRepository extends JpaRepository<Lookbook, Long> {

    // All published lookbooks, newest first (for customers)
    List<Lookbook> findByStatusOrderByCreatedAtDesc(LookbookStatus status);

    // All lookbooks for admin panel, newest first
    List<Lookbook> findAllByOrderByCreatedAtDesc();

    // Filter published lookbooks by season
    List<Lookbook> findByStatusAndSeasonIgnoreCaseOrderByCreatedAtDesc(
            LookbookStatus status, String season);

    // Filter published lookbooks by style
    List<Lookbook> findByStatusAndStyleIgnoreCaseOrderByCreatedAtDesc(
            LookbookStatus status, String style);

    // Search by title (admin + customer)
    List<Lookbook> findByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            LookbookStatus status, String title);
}