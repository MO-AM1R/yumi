package com.example.yumi.presentation.home.presenter;
import android.content.Context;

import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.data.plan.repository.MealPlanRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.domain.meals.model.MealsFilterType;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.base.BaseView;
import com.example.yumi.presentation.home.contract.SearchContract;
import com.example.yumi.utils.NetworkMonitor;

import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchPresenter extends BasePresenter<SearchContract.View>
    implements SearchContract.Presenter{

    private final MealsRepository repository;
    private final SearchContract.View view;
    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    public SearchPresenter(Context context, SearchContract.View view){
        this.view = view;
        repository = new MealsRepositoryImpl();
        mealPlanRepository = new MealPlanRepositoryImpl(context);
        userRepository = new UserRepositoryImpl(context);
    }

    private void onMealsLoaded(List<Meal> meals){
        withView(v -> {
            v.hideLoading();
            v.showMeals(meals);
        });
    }

    private void onCategoriesLoaded(List<Category> categories){
        withView(v -> {
            v.hideLoading();
            v.showCategories(categories);
        });
    }

    private void onIngredientsLoaded(List<Ingredient> ingredients){
        withView(v -> {
            v.hideLoading();
            v.showIngredients(ingredients);
        });
    }

    private void onAreasLoaded(List<Area> areas){
        withView(v -> {
            v.hideLoading();
            v.showAreas(areas);
        });
    }

    @Override
    public void onError(String error){
        withView(v -> {
            v.hideLoading();
            v.showError(error);
        });
    }

    @Override
    public boolean isGuestMode() {
        return userRepository.getCurrentUser().getUid().equalsIgnoreCase("Guest");
    }

    @Override
    public void loadMeals(String query) {
        if (!NetworkMonitor.INSTANCE.isConnected())
            return;

        withView(BaseView::showLoading);

        Disposable disposable = repository.searchMealsByName(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onMealsLoaded,
                        throwable -> onError(throwable.getLocalizedMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadCategories() {
        if (!NetworkMonitor.INSTANCE.isConnected())
            return;

        withView(BaseView::showLoading);

        Disposable disposable = repository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onCategoriesLoaded,
                        throwable -> onError(throwable.getLocalizedMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadAreas() {
        if (!NetworkMonitor.INSTANCE.isConnected())
            return;

        withView(BaseView::showLoading);

        Disposable disposable = repository.getAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onAreasLoaded,
                        throwable -> onError(throwable.getLocalizedMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadIngredients() {
        if (!NetworkMonitor.INSTANCE.isConnected())
            return;

        withView(BaseView::showLoading);

        Disposable disposable = repository.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onIngredientsLoaded,
                        throwable -> onError(throwable.getLocalizedMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetail(meal);
    }

    @Override
    public void onCategoryClicked(Category category) {
        view.navigateToFilteredMeals(new MealsFilter(MealsFilterType.CATEGORY, category.getName()));
    }

    @Override
    public void onIngredientClicked(Ingredient ingredient) {
        view.navigateToFilteredMeals(new MealsFilter(MealsFilterType.INGREDIENT, ingredient.getName()));
    }

    @Override
    public void addToMealPlan(Meal meal, String date, MealType mealType) {
        Disposable disposable = mealPlanRepository.addMealToPlan(date, mealType, meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    @Override
    public void onAreaClicked(Area area) {
        view.navigateToFilteredMeals(new MealsFilter(MealsFilterType.AREA, area.getName()));
    }
}
