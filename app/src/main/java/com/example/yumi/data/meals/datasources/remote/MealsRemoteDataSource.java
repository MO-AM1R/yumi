package com.example.yumi.data.meals.datasources.remote;
import com.example.yumi.data.meals.model.responses.AreasResponse;
import com.example.yumi.data.meals.model.responses.CategoriesResponse;
import com.example.yumi.data.meals.model.responses.IngredientsResponse;
import com.example.yumi.data.meals.model.responses.MealsResponse;
import io.reactivex.rxjava3.core.Single;


public interface MealsRemoteDataSource {
    Single<MealsResponse> geDayMeal();

    Single<MealsResponse> getMealById(String mealId);

    Single<MealsResponse> searchMealsByName(String mealName) ;

    Single<MealsResponse> searchMealsByFirstLetter(String firstLetter);

    Single<MealsResponse> filterMealsByCategory(String category);

    Single<MealsResponse> filterMealsByArea(String area);

    Single<MealsResponse> filterMealsByIngredient(String ingredient);

    Single<CategoriesResponse> getAllCategories();

    Single<AreasResponse> getAllAreas();

    Single<IngredientsResponse> getAllIngredients();
}
