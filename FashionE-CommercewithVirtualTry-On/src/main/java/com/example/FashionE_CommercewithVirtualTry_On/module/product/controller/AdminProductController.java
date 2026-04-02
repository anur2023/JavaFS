package com.example.FashionE_CommercewithVirtualTry_On.module.product.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.request.ProductRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.response.ProductResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // 🔹 Create Product
    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    // 🔹 Update Product
    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // 🔹 Delete Product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}