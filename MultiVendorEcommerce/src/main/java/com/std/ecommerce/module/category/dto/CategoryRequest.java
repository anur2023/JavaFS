package com.std.ecommerce.module.category.dto;

public class CategoryRequest {

    private String name;
    private String description;

    // 🔹 Default Constructor
    public CategoryRequest() {}

    // 🔹 Parameterized Constructor
    public CategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 🔹 Getters and Setters

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}