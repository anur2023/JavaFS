package com.example.FashionE_CommercewithVirtualTry_On.module.recommendation.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.response.ProductResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.recommendation.service.RecommendationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // 🔹 Get by category
    @GetMapping("/category/{category}")
    public List<ProductResponse> getByCategory(@PathVariable String category) {
        return recommendationService.recommendByCategory(category);
    }

    // 🔹 Get by brand
    @GetMapping("/brand/{brand}")
    public List<ProductResponse> getByBrand(@PathVariable String brand) {
        return recommendationService.recommendByBrand(brand);
    }

    // 🔹 Get similar products
    @GetMapping("/similar/{productId}")
    public List<ProductResponse> getSimilar(@PathVariable Long productId) {
        return recommendationService.recommendSimilar(productId);
    }
}