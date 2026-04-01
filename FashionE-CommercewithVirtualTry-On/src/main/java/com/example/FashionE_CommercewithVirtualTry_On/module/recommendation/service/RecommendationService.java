package com.example.FashionE_CommercewithVirtualTry_On.module.recommendation.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.response.ProductResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProductRepository productRepository;

    // 🔹 Recommend by Category
    public List<ProductResponse> recommendByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Recommend by Brand
    public List<ProductResponse> recommendByBrand(String brand) {
        List<Product> products = productRepository.findByBrand(brand);

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Recommend Similar Products
    public List<ProductResponse> recommendSimilar(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Product> products = productRepository.findByCategory(product.getCategory());

        return products.stream()
                .filter(p -> !p.getProductId().equals(productId)) // exclude same product
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Mapper
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setStyle(product.getStyle());
        response.setOccasion(product.getOccasion());
        response.setBrand(product.getBrand());
        response.setMaterialDetails(product.getMaterialDetails());
        response.setCareInstructions(product.getCareInstructions());
        response.setSizeChartId(product.getSizeChartId());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
}