package com.example.yumi.presentation.home.presenter;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.home.HomeContract;

import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private final MealsRepository repository;

    public HomePresenter() {
        this.repository = new MealsRepositoryImpl();
    }

    private void onDayMealLoaded(Meal meal) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showDayMeal(meal);
    }

    private void onCategoriesLoaded(List<Category> categories) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showCategories(categories);
    }

    private void onAreasLoaded(List<Area> areas) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showAreas(areas);
    }

    private void onRandomMealsLoaded(List<Meal> meals) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showRandomMeals(meals);
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
    public void loadDayMeal() {
        if (isViewDetached()) return;

        view.showLoading();

        Disposable disposable = repository.getDayMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onDayMealLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadRandomMeals() {
        if (isViewDetached()) return;

        view.showLoading();

        Random random = new Random();
        int index = random.nextInt(26) + 65;

        Disposable disposable = repository.getRandomMeals((char) index + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onRandomMealsLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadCategories() {
        if (isViewDetached()) return;

        view.showLoading();

        Disposable disposable = repository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onCategoriesLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadAreas() {
        if (isViewDetached()) return;

        view.showLoading();

        Disposable disposable = repository.getAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onAreasLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetail(meal.getId());
    }
}
