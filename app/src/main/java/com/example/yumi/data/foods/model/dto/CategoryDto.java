package com.example.yumi.data.foods.model.dto;

import com.example.yumi.domain.foods.model.Category;
import com.google.gson.annotations.SerializedName;

public class CategoryDto {
    @SerializedName("idCategory")
    private String id;

    @SerializedName("strCategory")
    private String name;

    @SerializedName("strCategoryThumb")
    private String thumbnailUrl;

    @SerializedName("strCategoryDescription")
    private String description;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public Category toCategory() {
        return new Category(
                orEmpty(id),
                orEmpty(name),
                orEmpty(thumbnailUrl),
                orEmpty(description)
        );
    }

    private String orEmpty(String str) {
        return str != null ? str : "";
    }
}