package com.example.yumi.presentation.home;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.base.BaseView;
import java.util.List;

public interface HomeContract {

    interface View extends BaseView {
        void showDayMeal(Meal meal);
        void showRandomMeals(List<Meal> meals);
        void showCategories(List<Category> categories);
        void showAreas(List<Area> areas);
        void navigateToMealDetail(String mealId);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadDayMeal();
        void loadRandomMeals();
        void loadCategories();
        void loadAreas();
        void onMealClicked(Meal meal);
        void onDestroy();
    }
}