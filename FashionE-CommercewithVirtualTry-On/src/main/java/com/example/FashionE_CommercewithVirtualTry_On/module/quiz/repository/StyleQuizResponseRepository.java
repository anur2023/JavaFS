package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.repository;

import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.entity.StyleQuizResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StyleQuizResponseRepository extends JpaRepository<StyleQuizResponse, Long> {
    List<StyleQuizResponse> findByUserUserId(Long userId);
    boolean existsByUserUserIdAndQuizQuizId(Long userId, Long quizId);
}