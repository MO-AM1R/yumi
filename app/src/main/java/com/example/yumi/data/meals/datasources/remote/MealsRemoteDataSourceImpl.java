package com.example.yumi.data.meals.datasources.remote;
import com.example.yumi.data.meals.datasources.remote.api.MealsServiceAPI;
import com.example.yumi.data.meals.model.responses.AreasResponse;
import com.example.yumi.data.meals.model.responses.CategoriesResponse;
import com.example.yumi.data.meals.model.responses.IngredientsResponse;
import com.example.yumi.data.meals.model.responses.MealsResponse;
import com.example.yumi.data.network.RetrofitClient;
import io.reactivex.rxjava3.core.Single;


public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource {
    private final MealsServiceAPI mealsServiceAPI;

    public MealsRemoteDataSourceImpl(){
        mealsServiceAPI = RetrofitClient.INSTANCE.getFoodApi();
    }

    public Single<MealsResponse> geDayMeal() {
        return mealsServiceAPI.getDayMeal();
    }

    public Single<MealsResponse> getMealById(String mealId){
        return mealsServiceAPI.getMealById(mealId);
    }

    public Single<MealsResponse> searchMealsByName(String mealName) {
        return mealsServiceAPI.searchMealsByName(mealName);
    }

    public Single<MealsResponse> searchMealsByFirstLetter(String firstLetter){
        return mealsServiceAPI.searchMealsByFirstLetter(firstLetter);
    }

    public Single<MealsResponse> filterMealsByCategory(String category){
        return mealsServiceAPI.filterMealsByCategory(category);
    }

    public Single<MealsResponse> filterMealsByArea(String area){
        return mealsServiceAPI.filterMealsByArea(area);
    }

    public Single<MealsResponse> filterMealsByIngredient(String ingredient) {
        return mealsServiceAPI.filterMealsByIngredient(ingredient);
    }

    public Single<CategoriesResponse> getAllCategories(){
        return mealsServiceAPI.getAllCategories();
    }

    public Single<AreasResponse> getAllAreas(){
        return mealsServiceAPI.getAllAreas();
    }

    public Single<IngredientsResponse> getAllIngredients(){
        return mealsServiceAPI.getAllIngredients();
    }
}