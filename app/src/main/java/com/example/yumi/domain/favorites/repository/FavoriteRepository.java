package com.example.yumi.domain.favorites.repository;

import com.example.yumi.domain.meals.model.Meal;

import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface FavoriteRepository {
    public Completable insertMeal(Meal meal) ;
    public Completable deleteMeal(String mealId);
    public Single<Boolean> onFavMeal(Meal meal) ;
    public Completable clearAllMeals();
    public Flowable<List<Meal>> getAllFavoriteMeals();
    public Flowable<Boolean> isFav(String mealId);
    Single<Set<String>> getAllFavoriteIds();
}
