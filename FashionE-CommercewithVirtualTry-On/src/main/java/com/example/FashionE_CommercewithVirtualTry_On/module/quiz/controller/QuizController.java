package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.controller;

import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.request.SubmitQuizRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.response.QuizSubmissionResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.response.StyleQuizQuestionResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping
    public ResponseEntity<List<StyleQuizQuestionResponse>> getAllQuestions() {
        return ResponseEntity.ok(quizService.getAllQuestions());
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizSubmissionResponse> submitAnswer(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SubmitQuizRequest request) {
        return ResponseEntity.ok(quizService.submitAnswer(userDetails.getUsername(), request));
    }

    @GetMapping("/my-responses")
    public ResponseEntity<List<QuizSubmissionResponse>> getMyResponses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizService.getMyResponses(userDetails.getUsername()));
    }
}