package com.example.yumi.presentation.home.presenter;
import android.content.Context;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.data.plan.repository.MealPlanRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.base.BaseView;
import com.example.yumi.presentation.home.contract.FavoriteContract;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class FavoritePresenter extends BasePresenter<FavoriteContract.View>
        implements FavoriteContract.Presenter {
    private final FavoriteRepository repository;
    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    public FavoritePresenter(Context context, FavoriteContract.View view) {
        repository = new FavoriteRepositoryImpl(context);
        mealPlanRepository = new MealPlanRepositoryImpl(context);
        userRepository = new UserRepositoryImpl(context);
        attachView(view);
    }

    private void onFavoriteMealsLoaded(List<Meal> meals) {
        withView(v -> {
            v.hideLoading();
            v.showFavoriteMeals(meals);
        });
    }

    @Override
    public void loadFavoriteMeals() {
        withView(BaseView::showLoading);

        Disposable disposable = repository.getAllFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onFavoriteMealsLoaded,
                        throwable ->
                                withView(v -> v.showError(throwable.getLocalizedMessage()))
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void onMealRemoved(Meal meal) {
        Disposable disposable = repository.deleteMeal(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

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
    public boolean isGuestMode(){
        return userRepository.getCurrentUser().getUid().equalsIgnoreCase("Guest");
    }
}
