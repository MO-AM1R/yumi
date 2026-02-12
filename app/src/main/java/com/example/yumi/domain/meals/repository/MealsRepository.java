package com.example.yumi.domain.meals.repository;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.plan.models.MealPlan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;


public interface MealsRepository {
    Single<Meal> getDayMeal();

    Single<List<Meal>> getRandomMeals(String letter);

    Single<Meal> getMealById(String mealId);

    Single<List<Meal>> searchMealsByName(String mealName);

    Single<List<Meal>> filterMealsByCategory(String category);

    Single<List<Meal>> filterMealsByArea(String area);

    Single<List<Meal>> filterMealsByIngredient(String ingredient);

    Single<List<Category>> getAllCategories();

    Single<List<Area>> getAllAreas();

    Single<List<Ingredient>> getAllIngredients();

    Completable clearAllLocalMeals();
}