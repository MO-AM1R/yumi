package com.example.yumi.domain.meals.model;

public class Category {
    private final String id;
    private final String name;
    private final String thumbnailUrl;
    private final String description;

    public Category(String id, String name, String thumbnailUrl, String description) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
    }

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
}