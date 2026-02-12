package com.example.yumi.data.user.datasources.remote;
import android.content.Context;
import com.example.yumi.data.firebase.auth.FirebaseAuthService;
import com.example.yumi.data.firebase.firestore.FirestoreService;
import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.plan.models.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class UserRemoteDataSourceImpl implements UserRemoteDataSource {
    private final FirebaseAuthService authService;
    private final FirestoreService firestoreService;

    public UserRemoteDataSourceImpl(FirebaseAuthService authService, FirestoreService firestoreService) {
        this.authService = authService;
        this.firestoreService = firestoreService;
    }


    @Override
    public Single<User> signInWithEmail(String email, String password) {
        return authService.signInWithEmail(email, password);
    }

    @Override
    public Single<User> signUpWithEmail(String email, String password, String displayName) {
        return authService.signUpWithEmail(email, password, displayName);
    }

    @Override
    public Single<User> signInWithGoogle(Context context) {
        return authService.signInWithGoogle(context);
    }

    @Override
    public Completable signOut() {
        return authService.signOut();
    }

    @Override
    public User getCurrentAuthUser() {
        return authService.getCurrentUser();
    }

    @Override
    public boolean isUserLoggedIn() {
        return authService.isUserLoggedIn();
    }

    @Override
    public String getCurrentUserId() {
        return authService.getCurrentUserId();
    }

    @Override
    public Completable createUser(User user) {
        return firestoreService.createUser(user);
    }

    @Override
    public Single<User> getUser(String userId) {
        return firestoreService.getUser(userId);
    }

    @Override
    public Completable deleteUser(String userId) {
        return firestoreService.deleteUser(userId);
    }

    @Override
    public Single<List<String>> getFavoriteMeals(String userId) {
        return firestoreService.getFavoriteMeals(userId);
    }

    @Override
    public Completable addFavoriteMeal(String userId, String mealId) {
        return firestoreService.addFavoriteMeal(userId, mealId);
    }

    @Override
    public Completable removeFavoriteMeal(String userId, String mealId) {
        return firestoreService.removeFavoriteMeal(userId, mealId);
    }

    @Override
    public Completable setFavoriteMeals(String userId, List<String> mealIds) {
        return firestoreService.setFavoriteMeals(userId, mealIds);
    }

    @Override
    public Single<MealPlan> getMealPlan(String userId) {
        return firestoreService.getMealPlan(userId);
    }

    @Override
    public Single<DayMeals> getDayMeals(String userId, String date) {
        return firestoreService.getDayMeals(userId, date);
    }

    @Override
    public Completable setDayMeals(String userId, String date, DayMeals dayMeals) {
        return firestoreService.setDayMeals(userId, date, dayMeals);
    }

    @Override
    public Completable removeDayMeals(String userId, String date) {
        return firestoreService.removeDayMeals(userId, date);
    }

    @Override
    public Completable setMealForDay(String userId, String date, MealType mealType, String mealId) {
        return firestoreService.setMealForDay(userId, date, mealType, mealId);
    }

    @Override
    public Completable removeMealFromDay(String userId, String date, MealType mealType) {
        return firestoreService.removeMealFromDay(userId, date, mealType);
    }

    @Override
    public Completable syncUserData(User user) {
        return firestoreService.syncUserData(user);
    }
}