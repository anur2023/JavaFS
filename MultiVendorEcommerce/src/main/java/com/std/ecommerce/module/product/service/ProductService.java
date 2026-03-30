package com.std.ecommerce.module.product.service;

import com.std.ecommerce.module.product.dto.ProductRequest;
import com.std.ecommerce.module.product.dto.ProductResponse;
import com.std.ecommerce.module.product.entity.Product;
import com.std.ecommerce.module.product.repository.ProductRepository;
import com.std.ecommerce.module.category.entity.Category;
import com.std.ecommerce.module.category.repository.CategoryREPO;
import com.std.ecommerce.module.vendor.entity.Vendor;
import com.std.ecommerce.module.vendor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CategoryREPO categoryRepository;


    public ProductResponse createProduct(ProductRequest request) {

        // Step 1: Validate and fetch the vendor
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + request.getVendorId()));

        // Step 2: Validate and fetch the category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        // Step 3: Business rule - prevent duplicate product names within same vendor
        if (productRepository.existsByNameAndVendorId(request.getName(), request.getVendorId())) {
            throw new RuntimeException("Product with name '" + request.getName() +
                    "' already exists in your store");
        }

        // Step 4: Create the product entity
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setVendor(vendor);
        product.setCategory(category);

        // Step 5: Save to database
        Product savedProduct = productRepository.save(product);

        // Step 6: Convert entity to response DTO and return
        return toResponse(savedProduct);
    }


    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return toResponse(product);
    }

    public List<ProductResponse> getProductsByVendor(Long vendorId) {
        // First validate that vendor exists
        if (!vendorRepository.existsById(vendorId)) {
            throw new RuntimeException("Vendor not found with id: " + vendorId);
        }

        return productRepository.findByVendorId(vendorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        // First validate that category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllProducts(); // If no search term, return all
        }

        return productRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public ProductResponse updateProduct(Long id, ProductRequest request) {

        // Step 1: Fetch the existing product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Step 2: Validate vendor exists (if vendor is being changed)
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + request.getVendorId()));

        // Step 3: Validate category exists (if category is being changed)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        // Step 4: Check for duplicate name only if name is changing
        if (!product.getName().equals(request.getName()) &&
                productRepository.existsByNameAndVendorId(request.getName(), request.getVendorId())) {
            throw new RuntimeException("Product with name '" + request.getName() +
                    "' already exists in your store");
        }

        // Step 5: Update all fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setVendor(vendor);
        product.setCategory(category);

        // Step 6: Save and return
        Product updatedProduct = productRepository.save(product);
        return toResponse(updatedProduct);
    }


    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productRepository.delete(product);
        return "Product deleted successfully";
    }


    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getVendor().getId(),
                product.getVendor().getStoreName(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}