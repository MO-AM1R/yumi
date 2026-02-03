package com.example.yumi.domain.meals.model;

public class Ingredient {
    private final String id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;
    private final String type;

    public Ingredient(String id, String name, String description,
                      String thumbnailUrl, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getType() {
        return type;
    }
}