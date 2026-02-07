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

    public void refreshData(){
        view.showLoading();

        loadDayMeal();
        loadRandomMeals();
    }

    public void loadHomeData() {
        view.showLoading();

        loadDayMeal();
        loadRandomMeals();
        loadIngredients();
        loadCategories();
        loadAreas();
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

    private void onIngredientsLoaded(List<Ingredient> ingredients) {
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
    public void loadDayMeal() {
        if (isViewDetached()) return;

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

        Random random = new Random();
        int index;
        do {
            index = random.nextInt(26) + 97;
        }while (index == 120);

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
    public void loadIngredients() {
        if (isViewDetached()) return;

        Disposable disposable = repository.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onIngredientsLoaded,
                        this::onError
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
