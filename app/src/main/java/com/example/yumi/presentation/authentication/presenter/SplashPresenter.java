package com.example.yumi.presentation.authentication.presenter;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.base.BasePresenter;


public class SplashPresenter extends BasePresenter<AuthContract.SplashView>
        implements AuthContract.SplashPresenter {
    private final UserRepository repository;
    private final Handler handler;
    private static final long MIN_SPLASH_DURATION = 500;
    private final AuthContract.SplashView view;

    public SplashPresenter(Application application, AuthContract.SplashView view) {
        this.repository = new UserRepositoryImpl(application.getApplicationContext());
        this.handler = new Handler(Looper.getMainLooper());
        this.view = view;
    }

    @Override
    public void onAnimationsComplete() {
        handler.postDelayed(this::checkAuthStateAndNavigate, MIN_SPLASH_DURATION);
    }

    private void checkAuthStateAndNavigate() {
        if (isViewDetached()) return;

        boolean onboardingCompleted = repository.isOnboardingCompleted();
        boolean isLoggedIn = repository.isLoggedIn();

        if (!onboardingCompleted) {
            view.navigateToOnboarding();
        } else if (isLoggedIn) {
            view.navigateToHome();
        } else {
            view.navigateToLogin();
        }
    }

    @Override
    public void detachView() {
        handler.removeCallbacksAndMessages(null);
        super.detachView();
    }
}