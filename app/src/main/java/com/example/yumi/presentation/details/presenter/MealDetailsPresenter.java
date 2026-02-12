package com.example.yumi.presentation.details.presenter;
import android.content.Context;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.data.plan.repository.MealPlanRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.details.view.MealDetailsContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealDetailsPresenter  extends BasePresenter<MealDetailsContract.View>
        implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;
    private final FavoriteRepository favoriteRepository;
    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;
    private final MealsRepository mealsRepository;
    private boolean isFavorite = false;

    public MealDetailsPresenter(Context context, MealDetailsContract.View view) {
        this.view = view;
        this.favoriteRepository = new FavoriteRepositoryImpl(context);
        this.mealPlanRepository = new MealPlanRepositoryImpl(context);
        this.mealsRepository = new MealsRepositoryImpl(context);
        this.userRepository = new UserRepositoryImpl(context);
    }

    @Override
    public void loadMealDetails(Meal meal) {
        if (view == null || meal == null) return;

        view.showMealDetails(meal);
        checkFavoriteStatus(meal.getId());

        if (isMealDataComplete(meal)) {
            displayFullMealInfo(meal);
        } else {
            fetchFullMealDetails(meal.getId());
        }
    }

    private boolean isMealDataComplete(Meal meal) {
        return meal.getInstructions() != null && !meal.getInstructions().isEmpty()
                && meal.getIngredients() != null && !meal.getIngredients().isEmpty();
    }

    private void fetchFullMealDetails(String mealId) {
        if (view != null) view.showLoading();

        Disposable disposable = mealsRepository.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        fullMeal -> {
                            if (view != null) {
                                view.hideLoading();
                                displayFullMealInfo(fullMeal);
                                view.showMealDetails(fullMeal);
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError("Failed to load full recipe: " + throwable.getMessage());
                            }
                        }
                );

        compositeDisposable.add(disposable);
    }

    private void displayFullMealInfo(Meal meal) {
        if (view == null) return;
        view.showIngredients(meal);
        view.showInstructions(meal);
        if (meal.getYoutubeUrl() != null && !meal.getYoutubeUrl().isEmpty()) {
            view.showVideoSection(meal.getYoutubeUrl());
        }
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (view == null || meal == null) return;
        if (isGuestMode()){
            view.showError("You are in guest mode");
            return;
        }

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
    public void checkFavoriteStatus(String mealId) {
        if (view == null || mealId == null) return;

        Disposable disposable = favoriteRepository.isFav(mealId)
                .firstOrError()
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
                            this.isFavorite = false;
                            if (view != null) {
                                view.updateFavoriteStatus(false);
                            }
                        }
                );

        compositeDisposable.add(disposable);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }

    @Override
    public boolean isGuestMode(){
        return userRepository.getCurrentUser().getUid().equalsIgnoreCase("Guest");
    }
}