package com.example.yumi.presentation.browse.presenter;

import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.browse.contracts.BrowseCountriesContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;


public class BrowseCountriesPresenter extends BasePresenter<BrowseCountriesContract.View>
        implements BrowseCountriesContract.Presenter {
    private final MealsRepository repository;


    public BrowseCountriesPresenter() {
        this.repository = new MealsRepositoryImpl();
    }

    private void onAreasLoaded(List<Area> areas) {
        if (isViewDetached()) return;

        view.hideLoading();
        view.showAreas(areas);
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
}
