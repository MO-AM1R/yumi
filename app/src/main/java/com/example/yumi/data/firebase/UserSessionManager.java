package com.example.yumi.data.firebase;
import com.example.yumi.domain.user.model.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.model.UserSettings;

import java.util.ArrayList;
import java.util.List;


public class UserSessionManager {

    private User currentUser;
    private MealPlan mealPlan;
    private boolean isInitialized = false;

    public UserSessionManager() {}


    public synchronized void setCurrentUser(User user) {
        this.currentUser = user;
        this.isInitialized = true;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    public synchronized boolean hasUser() {
        return currentUser != null;
    }

    public synchronized String getCurrentUserId() {
        return currentUser != null ? currentUser.getUid() : null;
    }


    public synchronized UserSettings getSettings() {
        if (currentUser != null && currentUser.getSettings() != null) {
            return currentUser.getSettings();
        }
        return new UserSettings();
    }

    public synchronized void updateSettings(UserSettings settings) {
        if (currentUser != null) {
            currentUser.setSettings(settings);
        }
    }


    public synchronized List<String> getFavoriteMealIds() {
        if (currentUser != null && currentUser.getFavoriteMealIds() != null) {
            return new ArrayList<>(currentUser.getFavoriteMealIds());
        }
        return new ArrayList<>();
    }

    public synchronized void setFavoriteMealIds(List<String> mealIds) {
        if (currentUser != null) {
            currentUser.setFavoriteMealIds(mealIds != null ? new ArrayList<>(mealIds) : new ArrayList<>());
        }
    }

    public synchronized void addFavoriteMeal(String mealId) {
        if (currentUser != null) {
            if (currentUser.getFavoriteMealIds() == null) {
                currentUser.setFavoriteMealIds(new ArrayList<>());
            }
            if (!currentUser.getFavoriteMealIds().contains(mealId)) {
                currentUser.getFavoriteMealIds().add(mealId);
            }
        }
    }

    public synchronized void removeFavoriteMeal(String mealId) {
        if (currentUser != null && currentUser.getFavoriteMealIds() != null) {
            currentUser.getFavoriteMealIds().remove(mealId);
        }
    }

    public synchronized boolean isFavorite(String mealId) {
        if (currentUser != null && currentUser.getFavoriteMealIds() != null) {
            return currentUser.getFavoriteMealIds().contains(mealId);
        }
        return false;
    }


    public synchronized MealPlan getMealPlan() {
        return mealPlan;
    }

    public synchronized void setMealPlan(MealPlan plan) {
        this.mealPlan = plan;
    }


    public synchronized void clearSession() {
        currentUser = null;
        mealPlan = null;
        isInitialized = false;
    }

    public synchronized boolean isInitialized() {
        return isInitialized;
    }

    public void setMealForDay(String date, MealType mealType, String mealId) {
        mealPlan.setMealForDay(date, mealType, mealId);
    }

    public void removeMealFromDay(String date, MealType mealType) {
        mealPlan.removeMealFromDay(date, mealType);
    }
}