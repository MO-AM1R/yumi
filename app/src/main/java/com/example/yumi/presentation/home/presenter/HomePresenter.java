package com.example.yumi.presentation.home.presenter;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.home.HomeContract;

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private final MealsRepository repository;

    public HomePresenter(MealsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadRandomMeals() {

    }

    @Override
    public void loadCategories() {

    }

    @Override
    public void loadAreas() {

    }

    @Override
    public void searchMeals(String query) {

    }

    @Override
    public void filterByCategory(String category) {

    }

    @Override
    public void filterByArea(String area) {

    }

    @Override
    public void onMealClicked(Meal meal) {

    }
}
