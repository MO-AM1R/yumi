package com.example.yumi.presentation.home.presenter;
import android.content.Context;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.domain.meals.model.MealsFilterType;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.base.BaseView;
import com.example.yumi.presentation.home.contract.HomeContract;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private final MealsRepository repository;
    private final FavoriteRepository favoriteRepository;
    private Set<String> favoriteIds = new HashSet<>();

    public HomePresenter(Context context) {
        this.repository = new MealsRepositoryImpl();
        this.favoriteRepository = new FavoriteRepositoryImpl(context);
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

    private static class MealsWithFavorites {
        final List<Meal> meals;
        final Set<String> favoriteIds;

        MealsWithFavorites(List<Meal> meals, Set<String> favoriteIds) {
            this.meals = meals;
            this.favoriteIds = favoriteIds;
        }
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

        Disposable disposable = Single.zip(
                        repository.getRandomMeals(letter),
                        favoriteRepository.getAllFavoriteIds(),
                        (meals, favIds) -> {
                            this.favoriteIds = favIds;
                            return new MealsWithFavorites(meals, favIds);
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> withView(v -> v.showRandomMeals(result.meals, result.favoriteIds)),
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
    public void onFavClicked(Meal meal, int position) {
        Disposable disposable = favoriteRepository.onFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> {
                            if (isFavorite) {
                                favoriteIds.add(meal.getId());
                            } else {
                                favoriteIds.remove(meal.getId());
                            }
                            withView(v -> v.updateFavoriteIcon(position, isFavorite));
                        },
                        throwable -> withView(v -> v.showError(throwable.getMessage()))
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void onFavClicked(Meal meal) {
        Disposable disposable = favoriteRepository.onFavMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> {
                            if (isFavorite) {
                                favoriteIds.add(meal.getId());
                            } else {
                                favoriteIds.remove(meal.getId());
                            }
                            withView(v -> v.updateFavoriteIcon(isFavorite));
                        },
                        throwable -> withView(v -> v.showError(throwable.getMessage()))
                );

        compositeDisposable.add(disposable);
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

    public boolean isFavoriteMeal(Meal meal) {
        return favoriteIds.contains(meal.getId());
    }
}
