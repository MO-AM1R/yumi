package com.example.yumi.presentation.home.presenter;
import android.content.Context;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
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

    public FavoritePresenter(Context context, FavoriteContract.View view) {
        repository = new FavoriteRepositoryImpl(context);
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
    public void onMealClicked(Meal meal) {

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
    public void onAddMealToPlan(Meal meal) {

    }
}
