package com.example.yumi.presentation.home.contract;

import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.base.BaseView;

import java.util.List;

public interface SearchContract {

    interface View extends BaseView {
        void showMeals(List<Meal> meals);
        void showCategories(List<Category> categories);
        void showAreas(List<Area> areas);
        void showIngredients(List<Ingredient> ingredients);

        void navigateToMealDetail(Meal meal);
        void navigateToFilteredMeals(MealsFilter filter);

        void showError(String error);
    }

    interface Presenter{
        void loadMeals(String query);
        void loadCategories();
        void loadAreas();
        void loadIngredients();

        void onMealClicked(Meal meal);
        void onCategoryClicked(Category category);
        void onIngredientClicked(Ingredient ingredient);
        void onAreaClicked(Area area);
        void addToMealPlan(Meal meal, String date, MealType mealType);
        void onError(String error);

        boolean isGuestMode();
    }
}
