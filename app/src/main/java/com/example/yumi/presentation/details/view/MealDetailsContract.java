package com.example.yumi.presentation.details.view;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;


public interface MealDetailsContract {
    interface View {
        void showMealDetails(Meal meal);
        void showIngredients(Meal meal);
        void showInstructions(Meal meal);
        void showVideoSection(String videoUrl);
        void updateFavoriteStatus(boolean isFavorite);
        void showAddToPlanSuccess();
        void showAddToPlanError(String message);
        void showAddToFavoriteSuccess();
        void showRemoveFromFavoriteSuccess();
        void showError(String message);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void loadMealDetails(Meal meal);
        void toggleFavorite(Meal meal);
        void addToMealPlan(Meal meal, String date, MealType mealType);
        void checkFavoriteStatus(String mealId);
        void onDestroy();
    }
}