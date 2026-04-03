package com.example.PharmacyOnlineStore.modules.medicine.dto;

import com.example.PharmacyOnlineStore.modules.medicine.entity.Category;

public class CategoryResponse {

    private String categoryId;
    private String categoryName;
    private String description;

    public static CategoryResponse from(Category category) {
        CategoryResponse res = new CategoryResponse();
        res.categoryId = category.getCategoryId();
        res.categoryName = category.getCategoryName();
        res.description = category.getDescription();
        return res;
    }

    public String getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getDescription() { return description; }
}