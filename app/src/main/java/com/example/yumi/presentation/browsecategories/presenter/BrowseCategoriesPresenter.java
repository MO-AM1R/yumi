package com.example.yumi.presentation.browsecategories.presenter;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.browsecategories.BrowseCategoriesContract;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class BrowseCategoriesPresenter extends BasePresenter<BrowseCategoriesContract.View>
        implements BrowseCategoriesContract.Presenter {
    private final MealsRepository repository;


    public BrowseCategoriesPresenter() {
        this.repository = new MealsRepositoryImpl();
    }

    private void onCategoriesLoaded(List<Category> categories) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showCategories(categories);
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
}
