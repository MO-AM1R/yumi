package com.example.yumi.domain.meals.model;

import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {
    private final String id;
    private final String name;
    private final String alternateName;
    private final String category;
    private final String area;
    private final String instructions;
    private final String thumbnailUrl;
    private final List<String> tags;
    private final String youtubeUrl;
    private final String sourceUrl;
    private final List<MealIngredient> ingredients;

    public Meal(String id, String name, String alternateName, String category,
                String area, String instructions, String thumbnailUrl,
                List<String> tags, String youtubeUrl, String sourceUrl,
                List<MealIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.alternateName = alternateName;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.thumbnailUrl = thumbnailUrl;
        this.tags = tags;
        this.youtubeUrl = youtubeUrl;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public List<MealIngredient> getIngredients() {
        return ingredients;
    }
}