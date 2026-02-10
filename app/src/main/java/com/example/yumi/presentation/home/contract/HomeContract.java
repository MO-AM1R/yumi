package com.example.yumi.presentation.home.contract;

import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.presentation.base.BaseView;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView {
        void showDayMeal(Meal meal);

        void showRandomMeals(List<Meal> meals);

        void showCategories(List<Category> categories);

        void showAreas(List<Area> areas);

        void showIngredients(List<Ingredient> ingredients);

        void navigateToMealDetail(Meal meal);

        void navigateToFilteredMeals(MealsFilter filter);

        void updateFavoriteIcon(int position, boolean isFavorite);
    }

    interface Presenter {
        void loadDayMeal();

        void loadRandomMeals();

        void loadCategories();

        void loadAreas();

        void loadIngredients();

        void onMealClicked(Meal meal);
        void onFavClicked(Meal meal, int position);

        void onCategoryClicked(Category category);

        void onIngredientClicked(Ingredient ingredient);

        void onAreaClicked(Area area);
    }
}