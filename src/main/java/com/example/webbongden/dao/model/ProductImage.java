package com.example.webbongden.dao.model;

public class ProductImage {
    private String url; // Đường dẫn hình ảnh
    private boolean mainImage; // Hình ảnh chính

    // Constructor
    public ProductImage(String url, boolean mainImage) {
        this.url = url;
        this.mainImage = mainImage;
    }

    public ProductImage() {}

    // Getters và Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMainImage() {
        return mainImage;
    }

    public void setMainImage(boolean mainImage) {
        this.mainImage = mainImage;
    }
}

