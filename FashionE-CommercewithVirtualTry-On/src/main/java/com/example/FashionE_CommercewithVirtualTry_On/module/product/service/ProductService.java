package com.example.FashionE_CommercewithVirtualTry_On.module.product.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.request.ProductRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.dto.response.ProductResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.entity.Product;
import com.example.FashionE_CommercewithVirtualTry_On.module.product.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 🔹 Create Product (ADMIN)
    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setStyle(request.getStyle());
        product.setOccasion(request.getOccasion());
        product.setBrand(request.getBrand());
        product.setMaterialDetails(request.getMaterialDetails());
        product.setCareInstructions(request.getCareInstructions());
        product.setSizeChartId(request.getSizeChartId());

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    // 🔹 Get All Products (USER)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Get Product by ID
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponse(product);
    }

    // 🔹 Update Product (ADMIN)
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setStyle(request.getStyle());
        product.setOccasion(request.getOccasion());
        product.setBrand(request.getBrand());
        product.setMaterialDetails(request.getMaterialDetails());
        product.setCareInstructions(request.getCareInstructions());
        product.setSizeChartId(request.getSizeChartId());

        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    // 🔹 Delete Product (ADMIN)
    public String deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);

        return "Product deleted successfully";
    }

    // 🔹 Mapper Method (Entity → Response)
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