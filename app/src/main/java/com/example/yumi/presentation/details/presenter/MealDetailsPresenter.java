package com.example.yumi.presentation.details.presenter;
import android.util.Log;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.details.view.MealDetailsContract;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;
    private final MealsRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private boolean isFavorite = false;

    public MealDetailsPresenter(MealDetailsContract.View view) {
        this.view = view;
        this.repository = new MealsRepositoryImpl();
    }

    @Override
    public void loadMealDetails(Meal meal) {
        if (view == null || meal == null) return;

        view.showMealDetails(meal);
        view.showIngredients(meal);
        view.showInstructions(meal);

        if (meal.getYoutubeUrl() != null && !meal.getYoutubeUrl().isEmpty()) {
            view.showVideoSection(meal.getYoutubeUrl());
        }

        checkFavoriteStatus(meal.getId());
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (view == null || meal == null) return;

        if (isFavorite) {
            removeFromFavorites(meal);
        } else {
            addToFavorites(meal);
        }
    }

    private void addToFavorites(Meal meal) {
        //TODO: add to favorite
    }

    private void removeFromFavorites(Meal meal) {
        //TODO: remove to favorite
    }

    @Override
    public void addToMealPlan(Meal meal, String date, MealType mealType) {
        //TODO: add to meal plan
    }

    @Override
    public void checkFavoriteStatus(String mealId) {
        //TODO: check Favorite status
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        view = null;
    }
}