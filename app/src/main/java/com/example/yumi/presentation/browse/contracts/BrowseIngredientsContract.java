package com.example.yumi.presentation.browse.contracts;

import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.presentation.base.BaseView;

import java.util.List;


public interface BrowseIngredientsContract {
    interface View extends BaseView {
        void showIngredients(List<Ingredient> ingredients);
    }

    interface Presenter{
        void loadIngredients();
    }
}
