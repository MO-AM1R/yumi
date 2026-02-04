package com.example.yumi.presentation.browseingredients.presenter;

import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.browseingredients.BrowseIngredientsContract;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class BrowseIngredientsPresenter extends BasePresenter<BrowseIngredientsContract.View>
        implements BrowseIngredientsContract.Presenter {
    private final MealsRepository repository;


    public BrowseIngredientsPresenter() {
        this.repository = new MealsRepositoryImpl();
    }

    private void onCategoriesLoaded(List<Ingredient> ingredients) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showIngredients(ingredients);
    }

    private void onError(Throwable throwable) {
        if (isViewDetached()) return;

        view.hideLoading();
        String errorMessage = throwable.getLocalizedMessage() != null
                ? throwable.getLocalizedMessage()
                : "An unexpected error occurred";
        view.showError(errorMessage);
    }

    @Override
    public void loadIngredients() {
        if (isViewDetached()) return;

        Disposable disposable = repository.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onCategoriesLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }
}
