package com.example.yumi.data.firebase.firestore;

import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.plan.models.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface FirestoreService {
    Completable createUser(User user);

    Single<User> getUser(String userId);

    Completable deleteUser(String userId);

    Single<List<String>> getFavoriteMeals(String userId);

    Completable addFavoriteMeal(String userId, String mealId);

    Completable removeFavoriteMeal(String userId, String mealId);

    Completable setFavoriteMeals(String userId, List<String> mealIds);

    Single<MealPlan> getMealPlan(String userId);

    Single<DayMeals> getDayMeals(String userId, String date);

    Completable setDayMeals(String userId, String date, DayMeals dayMeals);

    Completable removeDayMeals(String userId, String date);

    Completable setMealForDay(String userId, String date, MealType mealType, String mealId);

    Completable removeMealFromDay(String userId, String date, MealType mealType);

    Completable syncUserData(User user);
}
