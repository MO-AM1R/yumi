package com.example.yumi.data.database.doa;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.yumi.data.database.entity.MealIngredientEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;


@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertIngredients(List<MealIngredientEntity> ingredients);

    @Query("DELETE FROM ingredients WHERE mealOwnerId = :mealId")
    Completable deleteIngredientsByMealId(String mealId);
}
