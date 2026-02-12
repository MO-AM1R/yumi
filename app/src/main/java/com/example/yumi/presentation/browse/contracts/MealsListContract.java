package com.example.yumi.presentation.browse.contracts;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.base.BaseView;
import java.util.List;


public interface MealsListContract {
    interface View extends BaseView {
        void showMeals(List<Meal> meals);
    }

    interface Presenter{
        void loadMeals();
    }
}
