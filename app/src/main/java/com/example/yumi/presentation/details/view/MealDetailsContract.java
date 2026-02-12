package com.example.yumi.presentation.details.view;

import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.base.BaseView;

public interface MealDetailsContract {
    interface View extends BaseView {
        void showMealDetails(Meal meal);
        void showIngredients(Meal meal);
        void showInstructions(Meal meal);
        void showVideoSection(String videoUrl);
        void updateFavoriteStatus(boolean isFavorite);
        void showAddToPlanSuccess();
        void showAddToPlanError(String message);
    }

    interface Presenter {
        void loadMealDetails(Meal meal);
        void toggleFavorite(Meal meal);
        void addToMealPlan(Meal meal, String date, MealType mealType);
        void checkFavoriteStatus(String mealId);
        void onDestroy();
        boolean isGuestMode();
    }
}