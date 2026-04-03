package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.service;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import com.example.FashionE_CommercewithVirtualTry_On.module.auth.repository.UserRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.request.CreateQuizRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.request.SubmitQuizRequest;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.response.QuizSubmissionResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.response.StyleQuizQuestionResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.entity.StyleQuiz;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.entity.StyleQuizResponse;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.repository.StyleQuizRepository;
import com.example.FashionE_CommercewithVirtualTry_On.module.quiz.repository.StyleQuizResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private StyleQuizRepository quizRepository;

    @Autowired
    private StyleQuizResponseRepository responseRepository;

    @Autowired
    private UserRepository userRepository;

    private static final List<String> VALID_OPTIONS = List.of("A", "B", "C", "D");

    public List<StyleQuizQuestionResponse> getAllQuestions() {
        return quizRepository.findAll().stream()
                .map(q -> new StyleQuizQuestionResponse(
                        q.getQuizId(),
                        q.getQuestionText(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD()
                )).collect(Collectors.toList());
    }

    public QuizSubmissionResponse submitAnswer(String email, SubmitQuizRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StyleQuiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz question not found"));

        if (!VALID_OPTIONS.contains(request.getSelectedOption().toUpperCase())) {
            throw new RuntimeException("Selected option must be A, B, C or D");
        }

        if (responseRepository.existsByUserUserIdAndQuizQuizId(user.getUserId(), quiz.getQuizId())) {
            throw new RuntimeException("You have already answered this question");
        }

        StyleQuizResponse response = new StyleQuizResponse();
        response.setUser(user);
        response.setQuiz(quiz);
        response.setSelectedOption(request.getSelectedOption().toUpperCase());
        response.setTimestamp(LocalDateTime.now());

        StyleQuizResponse saved = responseRepository.save(response);

        return new QuizSubmissionResponse(
                saved.getResponseId(),
                quiz.getQuizId(),
                quiz.getQuestionText(),
                saved.getSelectedOption(),
                saved.getTimestamp()
        );
    }

    public List<QuizSubmissionResponse> getMyResponses(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return responseRepository.findByUserUserId(user.getUserId()).stream()
                .map(r -> new QuizSubmissionResponse(
                        r.getResponseId(),
                        r.getQuiz().getQuizId(),
                        r.getQuiz().getQuestionText(),
                        r.getSelectedOption(),
                        r.getTimestamp()
                )).collect(Collectors.toList());
    }

    // ─── Admin methods ────────────────────────────────────────────────────────

    public StyleQuizQuestionResponse createQuestion(CreateQuizRequest request) {
        StyleQuiz quiz = new StyleQuiz();
        quiz.setQuestionText(request.getQuestionText());
        quiz.setOptionA(request.getOptionA());
        quiz.setOptionB(request.getOptionB());
        quiz.setOptionC(request.getOptionC());
        quiz.setOptionD(request.getOptionD());

        StyleQuiz saved = quizRepository.save(quiz);

        return new StyleQuizQuestionResponse(
                saved.getQuizId(),
                saved.getQuestionText(),
                saved.getOptionA(),
                saved.getOptionB(),
                saved.getOptionC(),
                saved.getOptionD()
        );
    }

    public String deleteQuestion(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new RuntimeException("Quiz question not found");
        }
        quizRepository.deleteById(quizId);
        return "Quiz question deleted successfully";
    }
}