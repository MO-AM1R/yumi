package com.example.yumi.data.database.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites", foreignKeys = @ForeignKey(
        entity = MealEntity.class,
        parentColumns = "mealId",
        childColumns = "mealId"),
        indices = {@Index(value = "mealId")}
)
public class FavoriteEntity {
    @PrimaryKey
    @NonNull
    private final String mealId;

    private final long addedAt;

    public FavoriteEntity(@NonNull String mealId, long addedAt) {
        this.mealId = mealId;
        this.addedAt = addedAt;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    public long getAddedAt() {
        return addedAt;
    }
}
