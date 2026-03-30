package com.std.ecommerce.module.category.service;

import com.std.ecommerce.module.category.dto.CategoryRequest;
import com.std.ecommerce.module.category.dto.CategoryResponse;
import com.std.ecommerce.module.category.entity.Category;
import com.std.ecommerce.module.category.repository.CategoryREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryREPO categoryREPO;

    // 🔥 CREATE Category (Admin only)
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryREPO.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category saved = categoryREPO.save(category);
        return toResponse(saved);
    }

    // 🔥 GET All Categories (Public)
    public List<CategoryResponse> getAllCategories() {
        return categoryREPO.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🔥 GET Category by ID (Public)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryREPO.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return toResponse(category);
    }

    // 🔥 UPDATE Category (Admin only)
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryREPO.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Check name conflict only if name is changing
        if (!category.getName().equals(request.getName()) &&
                categoryREPO.existsByName(request.getName())) {
            throw new RuntimeException("Category name already taken: " + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryREPO.save(category);
        return toResponse(updated);
    }

    // 🔥 DELETE Category (Admin only)
    public String deleteCategory(Long id) {
        Category category = categoryREPO.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryREPO.delete(category);
        return "Category deleted successfully";
    }

    // 🔹 Helper: Entity → Response DTO
    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}