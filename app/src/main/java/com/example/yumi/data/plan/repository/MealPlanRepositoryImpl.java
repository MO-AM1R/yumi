package com.example.yumi.data.plan.repository;
import android.content.Context;
import com.example.yumi.data.database.mapper.MealPlannerMapper;
import com.example.yumi.data.plan.datasources.local.MealPlanLocalDataSource;
import com.example.yumi.data.plan.datasources.local.MealPlanLocalDataSourceImpl;
import com.example.yumi.domain.meals.model.Meal;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.user.model.MealType;
import java.util.Map;


public class MealPlanRepositoryImpl implements MealPlanRepository {

    private final MealPlanLocalDataSource localDataSource;

    public MealPlanRepositoryImpl(Context context) {
        this.localDataSource = new MealPlanLocalDataSourceImpl(context);
    }

    @Override
    public Completable addMealToPlan(String date, MealType mealType, Meal meal) {
        return localDataSource.addMealToPlan(date, mealType, meal);
    }

    @Override
    public Completable removeMealFromPlan(String date, MealType mealType) {
        return localDataSource.removeMealFromPlan(date, mealType);
    }

    @Override
    public Flowable<Map<MealType, Meal>> getMealsForDate(String date) {
        return localDataSource.getMealsForDate(date)
                .map(MealPlannerMapper::toPlanDayMap);
    }

    @Override
    public Flowable<Map<String, Map<MealType, Meal>>> getAllPlannedMeals() {
        return localDataSource.getAllPlanEntries()
                .map(MealPlannerMapper::toFullPlanMap);
    }

    @Override
    public Completable cleanupOldDays(String todayDate) {
        return localDataSource.deletePlanEntriesNotInDates(todayDate);
    }

    @Override
    public Completable clearAllPlannedMeals() {
        return localDataSource.clearAllPlanEntries();
    }
}