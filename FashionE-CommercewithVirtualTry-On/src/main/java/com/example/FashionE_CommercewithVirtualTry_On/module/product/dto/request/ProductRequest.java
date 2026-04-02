package com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.request;

public class ProductRequest {

    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;

    private String category;
    private String style;
    private String occasion;
    private String brand;

    private String materialDetails;
    private String careInstructions;

    private Long sizeChartId;

    // 🔹 Default Constructor
    public ProductRequest() {
    }

    // 🔹 Parameterized Constructor
    public ProductRequest(String name, String description, Double price, Integer stockQuantity,
                          String category, String style, String occasion, String brand,
                          String materialDetails, String careInstructions, Long sizeChartId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.style = style;
        this.occasion = occasion;
        this.brand = brand;
        this.materialDetails = materialDetails;
        this.careInstructions = careInstructions;
        this.sizeChartId = sizeChartId;
    }

    // 🔹 Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaterialDetails() {
        return materialDetails;
    }

    public void setMaterialDetails(String materialDetails) {
        this.materialDetails = materialDetails;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public Long getSizeChartId() {
        return sizeChartId;
    }

    public void setSizeChartId(Long sizeChartId) {
        this.sizeChartId = sizeChartId;
    }
}