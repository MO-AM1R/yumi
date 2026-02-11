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
import com.example.yumi.presentation.home.contract.HomeContract;
import java.util.List;
import java.util.Random;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private final MealsRepository repository;
    private final UserRepository userRepository;
    private final MealPlanRepository mealPlanRepository;

    public HomePresenter(Context context) {
        this.repository = new MealsRepositoryImpl();
        this.userRepository = new UserRepositoryImpl(context);
        this.mealPlanRepository = new MealPlanRepositoryImpl(context);
    }

    public void refreshData(){
        withView(BaseView::showLoading);

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
        withView(v -> {
            v.hideLoading();
            v.showDayMeal(meal);
        });
    }

    private void onError(Throwable throwable) {
        withView(v -> {
            v.hideLoading();
            String msg = throwable.getLocalizedMessage() != null
                    ? throwable.getLocalizedMessage()
                    : "Unexpected error";
            v.showError(msg);
        });
    }

    private void onCategoriesLoaded(List<Category> categories) {
        withView(v -> {
            v.hideLoading();
            v.showCategories(categories);
        });
    }

    private void onAreasLoaded(List<Area> areas) {
        withView(v -> {
            v.hideLoading();
            v.showAreas(areas);
        });
    }

    private void onIngredientsLoaded(List<Ingredient> ingredients) {
        withView(v -> {
            v.hideLoading();
            v.showIngredients(ingredients);
        });
    }

    private void onUserDetailsLoaded(String displayName) {
        withView(v -> v.showUserName(displayName));
    }

    @Override
    public void loadUserName() {
        String displayName = userRepository
                .getCurrentUser().getDisplayName()
                .substring(0, 2).toUpperCase();

        onUserDetailsLoaded(displayName);
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
        } while (index == 120);

        String letter = String.valueOf((char) index);

        Disposable disposable = repository.getRandomMeals(letter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> withView(v -> v.showRandomMeals(result)),
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
    public void addToMealPlan(Meal meal, String date, MealType mealType) {
        Disposable disposable = mealPlanRepository.addMealToPlan(date, mealType, meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

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
