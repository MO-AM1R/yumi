package com.example.yumi.presentation.browsecategories;

import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.presentation.base.BaseView;

import java.util.List;


public interface BrowseCategoriesContract {
    interface View extends BaseView {
        void showCategories(List<Category> categories);
    }

    interface Presenter{
        void loadCategories();
    }
}
