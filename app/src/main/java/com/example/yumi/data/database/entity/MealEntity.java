package com.example.yumi.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class MealEntity {
    @PrimaryKey
    @NonNull
    private final String mealId;
    @NonNull
    private final String name;
    @NonNull
    private final String category;
    @NonNull
    private final String area;
    @NonNull
    private final String instructions;
    @NonNull
    private final String thumbnailUrl;
    @NonNull
    private final String youtubeUrl;


    public MealEntity(@NonNull String mealId, @NonNull String name, @NonNull String category,
                      @NonNull String area, @NonNull String instructions,
                      @NonNull String thumbnailUrl, @NonNull String youtubeUrl) {
        this.mealId = mealId;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.thumbnailUrl = thumbnailUrl;
        this.youtubeUrl = youtubeUrl;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @NonNull
    public String getArea() {
        return area;
    }

    @NonNull
    public String getInstructions() {
        return instructions;
    }

    @NonNull
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @NonNull
    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}
