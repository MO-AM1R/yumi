package com.example.yumi.data.plan.datasources.local;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import com.example.yumi.data.database.pojo.PlanEntryWithMeal;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;



public interface MealPlanLocalDataSource {
    Completable addMealToPlan(String date, MealType mealType, Meal meal);

    Completable removeMealFromPlan(String date, MealType mealType);

    Flowable<List<PlanEntryWithMeal>> getMealsForDate(String date);

    Flowable<List<PlanEntryWithMeal>> getAllPlanEntries();

    Flowable<Boolean> hasMealForDateAndType(String date, MealType mealType);

    Completable deletePlanEntriesNotInDates(String todayDate);

    Completable clearAllPlanEntries();
}
