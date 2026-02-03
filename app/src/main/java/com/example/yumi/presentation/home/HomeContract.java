package com.example.yumi.presentation.home;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.base.BaseView;
import java.util.List;

public interface HomeContract {

    interface View extends BaseView {
        void showMeals(List<Meal> meals);
        void showCategories(List<Category> categories);
        void showAreas(List<Area> areas);
        void showEmptyState();
        void navigateToMealDetail(String mealId);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadRandomMeals();
        void loadCategories();
        void loadAreas();
        void searchMeals(String query);
        void filterByCategory(String category);
        void filterByArea(String area);
        void onMealClicked(Meal meal);
        void onDestroy();
    }
}