package com.example.yumi.presentation.authentication.presenter;

import android.app.Application;

import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.base.BasePresenter;


public class OnboardingPresenter extends BasePresenter<AuthContract.OnboardingView>
        implements AuthContract.OnboardingPresenter {

    private final UserRepository repository;
    private final AuthContract.OnboardingView view;

    public OnboardingPresenter(Application application, AuthContract.OnboardingView view) {
        this.repository = new UserRepositoryImpl(application.getApplicationContext());
        this.view = view;
    }


    @Override
    public void onNextClicked(int currentIndex) {
        if (isViewDetached()) return;

        int totalPages = view.getTotalPages();

        if (currentIndex >= totalPages - 1) {
            completeOnboarding();
        } else {
            view.showPage(currentIndex + 1);
        }
    }

    @Override
    public void onSkipClicked() {
        completeOnboarding();
    }

    private void completeOnboarding() {
        if (isViewDetached()) return;

        repository.setOnboardingCompleted(true);
        view.navigateToLogin();
    }
}