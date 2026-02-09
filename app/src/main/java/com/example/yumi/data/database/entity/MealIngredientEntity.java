package com.example.yumi.data.database.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients",
        foreignKeys = @ForeignKey(
                entity = MealEntity.class,
                parentColumns = "mealId",
                childColumns = "mealOwnerId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("mealOwnerId")
)
public class MealIngredientEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    private final String name;
    @NonNull
    private final String mealOwnerId;
    @NonNull
    private final String measure;
    @NonNull
    private final String thumbnailUrl;

    public MealIngredientEntity(@NonNull String name, @NonNull String mealOwnerId, @NonNull String measure, @NonNull String thumbnailUrl) {
        this.name = name;
        this.mealOwnerId = mealOwnerId;
        this.measure = measure;
        this.thumbnailUrl = thumbnailUrl;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getMealOwnerId() {
        return mealOwnerId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getMeasure() {
        return measure;
    }

    @NonNull
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
