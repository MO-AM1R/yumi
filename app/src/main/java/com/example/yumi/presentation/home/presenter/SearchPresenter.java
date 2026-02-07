package com.example.yumi.presentation.home.presenter;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.domain.meals.model.MealsFilterType;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.home.contract.SearchContract;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchPresenter extends BasePresenter<SearchContract.View>
    implements SearchContract.Presenter{

    private final MealsRepository repository;
    private final SearchContract.View view;

    public SearchPresenter(SearchContract.View view){
        this.view = view;
        repository = new MealsRepositoryImpl();
    }

    private void onMealsLoaded(List<Meal> meals){
        view.hideLoading();
        view.showMeals(meals);
    }

    private void onCategoriesLoaded(List<Category> categories){
        view.hideLoading();
        view.showCategories(categories);
    }

    private void onIngredientsLoaded(List<Ingredient> ingredients){
        view.hideLoading();
        view.showIngredients(ingredients);
    }

    private void onAreasLoaded(List<Area> areas){
        view.hideLoading();
        view.showAreas(areas);
    }

    @Override
    public void onError(String error){
        view.hideLoading();
        view.showError(error);
    }

    @Override
    public void loadMeals(String query) {
        view.showLoading();

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
        view.showLoading();

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
        view.showLoading();

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
        view.showLoading();

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
    public void onAreaClicked(Area area) {
        view.navigateToFilteredMeals(new MealsFilter(MealsFilterType.AREA, area.getName()));
    }
}
