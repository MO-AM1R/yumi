package com.example.yumi.data.database.doa;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(MealEntity meal);

    @Query("DELETE FROM meals WHERE mealId = :mealId")
    Completable deleteMeal(String mealId);

    @Query("DELETE FROM meals")
    Completable clearAllMeals();

    @Transaction
    @Query("SELECT * FROM meals")
    Flowable<List<MealWithIngredients>> getAllFavoriteMeals();

    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE mealId = :mealId)")
    Flowable<Boolean> isFav(String mealId);
}