package com.example.yumi.presentation.browse.presenter;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.domain.meals.model.MealsFilterType;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.browse.contracts.MealsListContract;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealsListPresenter extends BasePresenter<MealsListContract.View>
        implements MealsListContract.Presenter {

    private final MealsFilter filter;
    private final MealsRepository repository;
    private final MealsListContract.View view;


    public MealsListPresenter(MealsFilter filter, MealsListContract.View view) {
        this.repository = new MealsRepositoryImpl();
        this.filter = filter;
        this.view = view;
    }

    private void onMealsLoaded(List<Meal> meals) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showMeals(meals);
    }

    private void onError(Throwable throwable) {
        if (isViewDetached()) return;

        view.hideLoading();
        String errorMessage = throwable.getLocalizedMessage() != null
                ? throwable.getLocalizedMessage()
                : "An unexpected error occurred";
        view.showError(errorMessage);
    }

    private void filterByCategory(String category){
        Disposable disposable = repository.filterMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onMealsLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    private void filterByArea(String area){
        Disposable disposable = repository.filterMealsByArea(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onMealsLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    private void filterByIngredient(String ingredient){
        Disposable disposable = repository.filterMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onMealsLoaded,
                        this::onError
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadMeals() {
        if (isViewDetached()) return;

        if (filter.getType() == MealsFilterType.CATEGORY)
            filterByCategory(filter.getQuery());
        else if (filter.getType() == MealsFilterType.AREA)
            filterByArea(filter.getQuery());
        else if (filter.getType() == MealsFilterType.INGREDIENT)
            filterByIngredient(filter.getQuery());
    }
}
