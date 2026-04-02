package com.example.FashionE_CommercewithVirtualTry_On.module.product.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.response.ProductResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 🔹 Get all products
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    // 🔹 Get product by ID
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}