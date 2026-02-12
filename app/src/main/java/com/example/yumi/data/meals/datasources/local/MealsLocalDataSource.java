package com.example.yumi.data.meals.datasources.local;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.pojo.MealWithIngredients;

import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;


public interface MealsLocalDataSource {
    Maybe<MealWithIngredients> getMealById(String mealId);

    Single<Boolean> mealExists(String mealId);

    Completable clearAllMeals();
}
