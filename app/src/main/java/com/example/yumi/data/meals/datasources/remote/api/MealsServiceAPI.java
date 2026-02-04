package com.example.yumi.data.meals.datasources.remote.api;
import com.example.yumi.data.meals.model.responses.AreasResponse;
import com.example.yumi.data.meals.model.responses.CategoriesResponse;
import com.example.yumi.data.meals.model.responses.IngredientsResponse;
import com.example.yumi.data.meals.model.responses.MealsResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MealsServiceAPI {
    @GET("random.php")
    Single<MealsResponse> getDayMeal();

    @GET("lookup.php")
    Single<MealsResponse> getMealById(@Query("i") String mealId);

    @GET("search.php")
    Single<MealsResponse> searchMealsByName(@Query("s") String mealName);

    @GET("search.php")
    Single<MealsResponse> searchMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("filter.php")
    Single<MealsResponse> filterMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealsResponse> filterMealsByArea(@Query("a") String area);

    @GET("filter.php")
    Single<MealsResponse> filterMealsByIngredient(@Query("i") String ingredient);

    @GET("categories.php")
    Single<CategoriesResponse> getAllCategories();

    @GET("list.php?a=list")
    Single<AreasResponse> getAllAreas();

    @GET("list.php?i=list")
    Single<IngredientsResponse> getAllIngredients();
}