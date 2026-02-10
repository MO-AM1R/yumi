package com.example.yumi.domain.favorites.repository;
import com.example.yumi.domain.meals.model.Meal;
import java.util.List;
import java.util.Set;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public interface FavoriteRepository {
    Completable insertMeal(Meal meal);

    Completable deleteMeal(String mealId);

    Single<Boolean> onFavMeal(Meal meal);

    Completable clearAllFavorites();

    Flowable<List<Meal>> getAllFavoriteMeals();

    Flowable<Boolean> isFav(String mealId);

    Flowable<Set<String>> getAllFavoriteIds();
}
