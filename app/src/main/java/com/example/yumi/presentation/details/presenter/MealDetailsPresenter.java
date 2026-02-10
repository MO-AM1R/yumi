package com.example.yumi.presentation.details.presenter;

import android.content.Context;

import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.details.view.MealDetailsContract;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private boolean isFavorite = false;

    public MealDetailsPresenter(Context context, MealDetailsContract.View view) {
        this.view = view;
        this.favoriteRepository = new FavoriteRepositoryImpl(context);
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

        Disposable disposable = favoriteRepository.onFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newFavoriteState -> {
                            this.isFavorite = newFavoriteState;
                            if (view != null) {
                                view.updateFavoriteStatus(newFavoriteState);
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.showError(throwable.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void addToMealPlan(Meal meal, String date, MealType mealType) {
        // TODO: Implement add to meal plan
    }

    @Override
    public void checkFavoriteStatus(String mealId) {
        if (view == null || mealId == null) return;

        Disposable disposable = favoriteRepository.isFav(mealId)
                .firstOrError()  // Convert Flowable to Single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFav -> {
                            this.isFavorite = isFav;
                            if (view != null) {
                                view.updateFavoriteStatus(isFav);
                            }
                        },
                        throwable -> {
                            // Default to not favorite on error
                            this.isFavorite = false;
                            if (view != null) {
                                view.updateFavoriteStatus(false);
                            }
                        }
                );

        disposables.add(disposable);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        view = null;
    }
}