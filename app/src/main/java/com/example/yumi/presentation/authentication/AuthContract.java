package com.example.yumi.presentation.authentication;
import com.example.yumi.presentation.base.BaseView;


public interface AuthContract {
    interface SplashView extends BaseView{
        void navigateToOnboarding();
        void navigateToLogin();
        void navigateToHome();
    }

    interface SplashPresenter {
        void attachView(SplashView view);
        void detachView();
        void onAnimationsComplete();
    }

    interface OnboardingView extends BaseView {
        void showPage(int index);
        void navigateToLogin();
        int getCurrentPageIndex();
        int getTotalPages();
    }

    interface OnboardingPresenter {
        void attachView(OnboardingView view);
        void detachView();
        void onNextClicked(int currentIndex);
        void onSkipClicked();
    }

    interface LoginView extends BaseView {
        void showEmailError(String error);
        void showPasswordError(String error);
        void clearErrors();
        void onLoginSuccess();
        void showSuccess(String message);
    }

    interface LoginPresenter {
        void attachView(LoginView view);
        void detachView();
        void login(String email, String password);
        void loginWithGoogle(android.content.Context context);
    }

    interface RegisterView extends BaseView {
        void showUsernameError(String error);
        void showEmailError(String error);
        void showPasswordError(String error);
        void clearErrors();
        void onSignUpSuccess();
        void showSuccess(String message);
    }

    interface RegisterPresenter {
        void attachView(RegisterView view);
        void detachView();
        void signUp(String name, String email, String password);
        void signUpWithGoogle(android.content.Context context);
    }
}