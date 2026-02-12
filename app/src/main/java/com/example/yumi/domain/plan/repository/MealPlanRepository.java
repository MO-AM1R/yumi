package com.example.yumi.domain.plan.repository;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import java.util.List;
import java.util.Map;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public interface MealPlanRepository {
    Completable addMealToPlan(String date, MealType mealType, Meal meal);

    Completable removeMealFromPlan(String date, MealType mealType);

    Flowable<Map<MealType, Meal>> getMealsForDate(String date);

    Flowable<Map<String, Map<MealType, Meal>>> getAllPlannedMeals();

    Completable cleanupOldDays(String todayDate);

    Completable clearAllPlannedMeals();
}
