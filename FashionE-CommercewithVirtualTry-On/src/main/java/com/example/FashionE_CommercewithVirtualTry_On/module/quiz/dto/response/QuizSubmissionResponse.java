package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.response;

import java.time.LocalDateTime;

public class QuizSubmissionResponse {
    private Long responseId;
    private Long quizId;
    private String questionText;
    private String selectedOption;
    private LocalDateTime timestamp;

    public QuizSubmissionResponse() {}

    public QuizSubmissionResponse(Long responseId, Long quizId, String questionText,
                                  String selectedOption, LocalDateTime timestamp) {
        this.responseId = responseId;
        this.quizId = quizId;
        this.questionText = questionText;
        this.selectedOption = selectedOption;
        this.timestamp = timestamp;
    }

    public Long getResponseId() { return responseId; }
    public void setResponseId(Long responseId) { this.responseId = responseId; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}