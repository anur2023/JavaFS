package com.example.FashionE_CommercewithVirtualTry_On.module.inspiration.dto.request;

public class InspirationRequest {

    private String title;
    private String contentText;
    private String imageUrl;

    // 🔹 Default Constructor
    public InspirationRequest() {
    }

    // 🔹 Parameterized Constructor
    public InspirationRequest(String title, String contentText, String imageUrl) {
        this.title = title;
        this.contentText = contentText;
        this.imageUrl = imageUrl;
    }

    // 🔹 Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}