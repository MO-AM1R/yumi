package com.example.yumi.presentation.home.contract;

import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.presentation.base.BaseView;

import java.util.List;
import java.util.Set;

public interface HomeContract {

    interface View extends BaseView {
        void showDayMeal(Meal meal);

        void showCategories(List<Category> categories);

        void showAreas(List<Area> areas);

        void showIngredients(List<Ingredient> ingredients);

        void navigateToMealDetail(Meal meal);

        void navigateToFilteredMeals(MealsFilter filter);

        void updateFavoriteIcon(int position, boolean isFavorite);
        void updateFavoriteIcon(boolean isFavorite);
        void showUserName(String displayName);
        void showRandomMeals(List<Meal> meals, Set<String> favoriteIds);
    }

    interface Presenter {
        void loadDayMeal();
        void loadUserName();

        void loadRandomMeals();

        void loadCategories();

        void loadAreas();

        void loadIngredients();

        void onMealClicked(Meal meal);
        void onFavClicked(Meal meal, int position);
        void onFavClicked(Meal meal);

        void onCategoryClicked(Category category);

        void onIngredientClicked(Ingredient ingredient);

        void onAreaClicked(Area area);
    }
}