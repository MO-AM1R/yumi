package com.example.yumi.data.foods.datasources.remote.api;
import com.example.yumi.data.foods.model.responses.AreasResponse;
import com.example.yumi.data.foods.model.responses.CategoriesResponse;
import com.example.yumi.data.foods.model.responses.IngredientsResponse;
import com.example.yumi.data.foods.model.responses.MealsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodServiceAPI {

    @GET("random.php")
    Call<MealsResponse> getRandomMeal();

    @GET("lookup.php")
    Call<MealsResponse> getMealById(@Query("i") String mealId);

    @GET("search.php")
    Call<MealsResponse> searchMealsByName(@Query("s") String mealName);

    @GET("search.php")
    Call<MealsResponse> searchMealsByFirstLetter(@Query("f") String firstLetter);

    @GET("filter.php")
    Call<MealsResponse> filterMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealsResponse> filterMealsByArea(@Query("a") String area);

    @GET("filter.php")
    Call<MealsResponse> filterMealsByIngredient(@Query("i") String ingredient);

    @GET("categories.php")
    Call<CategoriesResponse> getAllCategories();

    @GET("list.php?a=list")
    Call<AreasResponse> getAllAreas();

    @GET("list.php?i=list")
    Call<IngredientsResponse> getAllIngredients();
}