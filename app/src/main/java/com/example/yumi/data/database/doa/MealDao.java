package com.example.yumi.data.database.doa;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(MealEntity meal);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealIgnore(MealEntity meal);

    @Transaction
    @Query("SELECT * FROM meals WHERE mealId = :mealId")
    Maybe<MealWithIngredients> getMealById(String mealId);

    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE mealId = :mealId)")
    Single<Boolean> mealExists(String mealId);

    @Query("DELETE FROM meals WHERE mealId = :mealId")
    Completable deleteMeal(String mealId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE mealId = :mealId)")
    Single<Boolean> isMealInFavorites(String mealId);

    @Query("SELECT EXISTS(SELECT 1 FROM meal_plan WHERE mealId = :mealId)")
    Single<Boolean> isMealInPlan(String mealId);

    @Query("SELECT NOT EXISTS(SELECT 1 FROM favorites WHERE mealId = :mealId) " +
            "AND NOT EXISTS(SELECT 1 FROM meal_plan WHERE mealId = :mealId)")
    Single<Boolean> isMealOrphan(String mealId);

    @Query("DELETE FROM meals")
    Completable clearAllMeals();
}