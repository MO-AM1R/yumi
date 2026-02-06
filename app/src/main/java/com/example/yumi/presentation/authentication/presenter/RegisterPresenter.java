package com.example.yumi.presentation.authentication.presenter;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.base.BasePresenter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RegisterPresenter extends BasePresenter<AuthContract.RegisterView>
        implements AuthContract.RegisterPresenter {

    private final UserRepository repository;
    private final AuthContract.RegisterView view;

    public RegisterPresenter(Application application, AuthContract.RegisterView view) {
        this.repository = new UserRepositoryImpl(application.getApplicationContext());
        this.view = view;
    }

    @Override
    public void signUp(String name, String email, String password) {
        if (isViewDetached()) return;

        view.clearErrors();

        if (!validateInput(name, email, password)) return;

        view.showLoading();

        compositeDisposable.add(
                repository.signUpWithEmail(email, password, name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.onSignUpSuccess();
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
    public void signUpWithGoogle(Context context) {
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
                                        view.onSignUpSuccess();
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

    private boolean validateInput(String name, String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            view.showUsernameError("Name is required");
            valid = false;
        } else if (name.length() < 2) {
            view.showUsernameError("Name must be at least 2 characters");
            valid = false;
        }

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