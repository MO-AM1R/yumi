package com.example.yumi.presentation.base;

public interface BaseView {
    void showLoading();
    void hideLoading();
    void showError(String message);
}