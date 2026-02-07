package com.example.yumi.domain.meals.model;

import java.io.Serializable;

public class MealIngredient implements Serializable {
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
}