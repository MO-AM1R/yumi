package com.example.yumi.presentation.authentication.presenter;

import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.base.BasePresenter;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LoginPresenter extends BasePresenter<AuthContract.LoginView>
        implements AuthContract.LoginPresenter {

    private final UserRepository repository;
    private final AuthContract.LoginView view;

    public LoginPresenter(Application application, AuthContract.LoginView view) {
        this.repository = new UserRepositoryImpl(application.getApplicationContext());
        this.view = view;
    }

    @Override
    public void login(String email, String password) {
        if (isViewDetached()) return;

        view.clearErrors();

        if (!validateInput(email, password)) return;

        view.showLoading();

        compositeDisposable.add(
                repository.signInWithEmail(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.onLoginSuccess();
                                    }
                                },
                                throwable -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.showError(throwable.getLocalizedMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void loginWithGoogle(Context context) {
        if (isViewDetached()) return;

        view.showLoading();

        compositeDisposable.add(
                repository.signInWithGoogle(context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.onLoginSuccess();
                                    }
                                },
                                throwable -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.showError(throwable.getLocalizedMessage());
                                    }
                                }
                        )
        );
    }

    private boolean validateInput(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            view.showEmailError("Email is required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showEmailError("Invalid email address");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            view.showPasswordError("Password is required");
            valid = false;
        } else if (password.length() < 6) {
            view.showPasswordError("Password must be at least 6 characters");
            valid = false;
        }

        return valid;
    }
}