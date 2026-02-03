package com.example.yumi.domain.foods.model;

import com.example.yumi.data.config.APIConfig;

public class MealIngredient {
    private final String name;
    private final String measure;
    private final String thumbnailUrl;

    public MealIngredient(String name, String measure, String thumbnailUrl) {
        this.name = name;
        this.measure = measure;
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getImageUrl() {
        if (name == null || name.isEmpty()) {
            return "";
        }

        return APIConfig.INGREDIENTS_IMAGE_BASE_URL + name + ".png";
    }
}