package com.example.PharmacyOnlineStore.modules.medicine.service;

import com.example.PharmacyOnlineStore.common.exception.ConflictException;
import com.example.PharmacyOnlineStore.common.exception.ResourceNotFoundException;
import com.example.PharmacyOnlineStore.modules.medicine.dto.CategoryRequest;
import com.example.PharmacyOnlineStore.modules.medicine.dto.CategoryResponse;
import com.example.PharmacyOnlineStore.modules.medicine.entity.Category;
import com.example.PharmacyOnlineStore.modules.medicine.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByCategoryNameIgnoreCase(request.getCategoryName())) {
            throw new ConflictException("Category already exists: " + request.getCategoryName());
        }
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public CategoryResponse getById(String id) {
        return CategoryResponse.from(findById(id));
    }

    public CategoryResponse update(String id, CategoryRequest request) {
        Category category = findById(id);
        categoryRepository.findByCategoryNameIgnoreCase(request.getCategoryName())
                .ifPresent(existing -> {
                    if (!existing.getCategoryId().equals(id)) {
                        throw new ConflictException("Category name already taken");
                    }
                });
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void delete(String id) {
        categoryRepository.delete(findById(id));
    }

    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}