package com.example.FashionE_CommercewithVirtualTry_On.module.quiz.dto.request;

public class SubmitQuizRequest {
    private Long quizId;
    private String selectedOption;

    public SubmitQuizRequest() {}

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
}