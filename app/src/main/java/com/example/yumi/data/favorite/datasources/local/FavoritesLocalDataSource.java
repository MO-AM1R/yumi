package com.example.yumi.data.favorite.datasources.local;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public interface FavoritesLocalDataSource {
    Completable insertMeal(MealWithIngredients meal);
    Completable deleteMeal(String mealId);
    Completable clearAllMeals();
    Flowable<List<MealWithIngredients>> getAllFavoriteMeals();
    Flowable<Boolean> isFav(String mealId);
}
