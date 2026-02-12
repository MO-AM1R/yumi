package com.example.yumi.data.database.doa;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.yumi.data.database.entity.MealPlanEntity;
import com.example.yumi.data.database.pojo.PlanEntryWithMeal;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlanEntry(MealPlanEntity planEntry);

    @Query("DELETE FROM meal_plan WHERE date = :date AND mealType = :mealType")
    Completable deletePlanEntry(String date, String mealType);

    @Query("DELETE FROM meal_plan WHERE date < :todayDate")
    Completable deleteOldMeals(String todayDate);

    @Query("DELETE FROM meal_plan")
    Completable clearAllPlanEntries();

    @Transaction
    @Query("SELECT * FROM meal_plan WHERE date = :date")
    Flowable<List<PlanEntryWithMeal>> getPlanEntriesForDate(String date);

    @Transaction
    @Query("SELECT * FROM meal_plan ORDER BY date ASC")
    Flowable<List<PlanEntryWithMeal>> getAllPlanEntries();

    @Query("SELECT EXISTS(SELECT 1 FROM meal_plan WHERE date = :date AND mealType = :mealType)")
    Flowable<Boolean> hasPlanEntry(String date, String mealType);

    @Query("SELECT mealId FROM meal_plan WHERE date = :date AND mealType = :mealType")
    Single<String> getMealIdForPlanEntry(String date, String mealType);
}
