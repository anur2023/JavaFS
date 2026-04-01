package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.response.InspirationResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.service.InspirationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspiration")
public class InspirationController {

    @Autowired
    private InspirationService inspirationService;

    // 🔹 Get all posts
    @GetMapping
    public List<InspirationResponse> getAllPosts() {
        return inspirationService.getAllPosts();
    }

    // 🔹 Get post by ID
    @GetMapping("/{id}")
    public InspirationResponse getPostById(@PathVariable Long id) {
        return inspirationService.getPostById(id);
    }
}