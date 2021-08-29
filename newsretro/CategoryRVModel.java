package com.example.newsretro;

public class CategoryRVModel {

    private String category;
    private String categoryImagUrl;

    public CategoryRVModel(String category, String categoryImageUrl) {
        this.category = category;
        this.categoryImagUrl = categoryImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryImageUrl() {
        return categoryImagUrl;
    }

    public void setCategoryImageUrl(String categoryImageurl) {
        this.categoryImagUrl = categoryImageurl;
    }
}
