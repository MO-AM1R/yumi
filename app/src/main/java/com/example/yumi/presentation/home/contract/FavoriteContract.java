package com.example.yumi.presentation.home.contract;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.base.BaseView;
import java.util.List;


public interface FavoriteContract {
    interface View extends BaseView {
        void showFavoriteMeals(List<Meal> meals);
        void navigateToMealDetail(Meal meal);
    }

    interface Presenter {
        void loadFavoriteMeals();
        void onMealRemoved(Meal meal);
        void addToMealPlan(Meal meal, String date, MealType mealType);
    }
}
