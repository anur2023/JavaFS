package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.entity;

import com.example.FashionE_CommercewithVirtualTry_On.module.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "style_quiz_responses")
public class StyleQuizResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private StyleQuiz quiz;

    @Column(nullable = false)
    private String selectedOption;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public StyleQuizResponse() {}

    public Long getResponseId() { return responseId; }
    public void setResponseId(Long responseId) { this.responseId = responseId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public StyleQuiz getQuiz() { return quiz; }
    public void setQuiz(StyleQuiz quiz) { this.quiz = quiz; }

    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}