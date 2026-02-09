package com.example.yumi.domain.favorites.repository;

import com.example.yumi.domain.meals.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface FavoriteRepository {
    public Completable insertMeal(Meal meal) ;
    public Completable deleteMeal(String mealId);
    public Completable clearAllMeals();
    public Flowable<List<Meal>> getAllFavoriteMeals();
    public Flowable<Boolean> isFav(String mealId);
}
