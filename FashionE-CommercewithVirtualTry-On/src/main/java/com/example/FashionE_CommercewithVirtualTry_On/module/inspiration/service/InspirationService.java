package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.request.InspirationRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.response.InspirationResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.entity.Inspiration;
import com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.repository.InspirationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspirationService {

    @Autowired
    private InspirationRepository inspirationRepository;

    // 🔹 Create Inspiration Post (ADMIN)
    public InspirationResponse createPost(InspirationRequest request) {

        Inspiration inspiration = new Inspiration();
        inspiration.setTitle(request.getTitle());
        inspiration.setContentText(request.getContentText());
        inspiration.setImageUrl(request.getImageUrl());
        inspiration.setCreatedAt(LocalDateTime.now());

        Inspiration saved = inspirationRepository.save(inspiration);

        return mapToResponse(saved);
    }

    // 🔹 Get All Posts (USER)
    public List<InspirationResponse> getAllPosts() {

        List<Inspiration> list =
                inspirationRepository.findAllByOrderByCreatedAtDesc();

        return list.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Get Post by ID
    public InspirationResponse getPostById(Long id) {

        Inspiration inspiration = inspirationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return mapToResponse(inspiration);
    }

    // 🔹 Update Post (ADMIN)
    public InspirationResponse updatePost(Long id, InspirationRequest request) {

        Inspiration inspiration = inspirationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        inspiration.setTitle(request.getTitle());
        inspiration.setContentText(request.getContentText());
        inspiration.setImageUrl(request.getImageUrl());

        Inspiration updated = inspirationRepository.save(inspiration);

        return mapToResponse(updated);
    }

    // 🔹 Delete Post (ADMIN)
    public String deletePost(Long id) {

        Inspiration inspiration = inspirationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        inspirationRepository.delete(inspiration);

        return "Post deleted successfully";
    }

    // 🔹 Mapper (Entity → Response)
    private InspirationResponse mapToResponse(Inspiration inspiration) {

        InspirationResponse response = new InspirationResponse();

        response.setFeedId(inspiration.getFeedId());
        response.setTitle(inspiration.getTitle());
        response.setContentText(inspiration.getContentText());
        response.setImageUrl(inspiration.getImageUrl());
        response.setCreatedAt(inspiration.getCreatedAt());

        return response;
    }
}