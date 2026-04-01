package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.request.InspirationRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.response.InspirationResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.service.InspirationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/inspiration")
public class AdminInspirationController {

    @Autowired
    private InspirationService inspirationService;

    // 🔹 Create Post
    @PostMapping
    public InspirationResponse createPost(@RequestBody InspirationRequest request) {
        return inspirationService.createPost(request);
    }

    // 🔹 Update Post
    @PutMapping("/{id}")
    public InspirationResponse updatePost(@PathVariable Long id,
                                          @RequestBody InspirationRequest request) {
        return inspirationService.updatePost(id, request);
    }

    // 🔹 Delete Post
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        return inspirationService.deletePost(id);
    }
}