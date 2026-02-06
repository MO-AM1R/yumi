package com.example.yumi.domain.user.repository;

import android.content.Context;

import com.example.yumi.domain.user.model.DayMeals;
import com.example.yumi.domain.user.model.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.model.UserSettings;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    Single<User> signInWithEmail(String email, String password);
    Single<User> signUpWithEmail(String email, String password, String displayName);

    Single<User> signInWithGoogle(Context context);

    Completable signOut();
    User getCurrentUser();
    boolean isLoggedIn();
    String getCurrentUserId();

    void setOnboardingCompleted(boolean completed);
    boolean isOnboardingCompleted();

    Single<UserSettings> getUserSettings();
    Completable updateUserSettings(UserSettings settings);

    Single<List<String>> getFavoriteMeals();
    Completable addFavoriteMeal(String mealId);
    Completable removeFavoriteMeal(String mealId);
    boolean isFavorite(String mealId);

    Single<MealPlan> getMealPlan();
    Single<DayMeals> getDayMeals(String date);
    Completable setDayMeals(String date, DayMeals dayMeals);
    Completable setMealForDay(String date, MealType mealType, String mealId);
    Completable removeMealFromDay(String date, MealType mealType);
}
