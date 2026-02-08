package com.example.yumi.presentation.base;
import java.util.function.Consumer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends BaseView> {

    protected V view;
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        compositeDisposable.clear();
        this.view = null;
    }

    public void onDestroy() {
        compositeDisposable.clear();
        detachView();
    }

    protected boolean isViewDetached() {
        return view == null;
    }

    protected void withView(Consumer<V> action) {
        if (view != null) {
            action.accept(view);
        }
    }
}