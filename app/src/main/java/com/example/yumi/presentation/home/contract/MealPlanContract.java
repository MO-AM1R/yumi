package com.example.yumi.presentation.home.contract;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.plan.models.PlanDay;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.base.BaseView;
import java.util.List;

public interface MealPlanContract {

    interface View extends BaseView {
        void showDays(List<PlanDay> days);
        void showSelectedDate(String fullDate);
        void showMealForType(MealType mealType, Meal meal);
        void showEmptyMealType(MealType mealType);
        void updateDaySelection(int previousPosition, int newPosition);
        void showMealRemovedSuccess();
        void showMealAddedSuccess();
        void navigateToAddMeal(String date, MealType mealType);
        void navigateToMealDetails(Meal meal);
    }

    interface Presenter {
        void loadMealPlan();
        void onDaySelected(int position);
        void onAddMealClicked(MealType mealType);
        void onRemoveMealClicked(MealType mealType);
        void addMealToPlan(String dateKey, MealType mealType, Meal meal);
        void cleanupOldDays();
        void onDestroy();
    }
}