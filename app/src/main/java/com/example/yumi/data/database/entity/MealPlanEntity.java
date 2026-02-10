package com.example.yumi.data.database.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "meal_plan",
        primaryKeys = {"date", "mealType"},
        foreignKeys = @ForeignKey(
                entity = MealEntity.class,
                parentColumns = "mealId",
                childColumns = "mealId"
        ),
        indices = {@Index(value = "mealId")}
)
public class MealPlanEntity {
    @NonNull
    private final String date;

    @NonNull
    private final String mealType;

    @NonNull
    private final String mealId;

    public MealPlanEntity(
            @NonNull String date,
            @NonNull String mealType,
            @NonNull String mealId) {
        this.date = date;
        this.mealType = mealType;
        this.mealId = mealId;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getMealType() {
        return mealType;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }
}
