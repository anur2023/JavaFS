package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.request.CreateQuizRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.response.StyleQuizQuestionResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/quiz")
public class AdminQuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<StyleQuizQuestionResponse> createQuestion(
            @RequestBody CreateQuizRequest request) {
        return ResponseEntity.ok(quizService.createQuestion(request));
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.deleteQuestion(quizId));
    }
}