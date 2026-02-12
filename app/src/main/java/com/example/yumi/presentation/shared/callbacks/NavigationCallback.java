package com.example.yumi.presentation.shared.callbacks;

import androidx.fragment.app.Fragment;

public interface NavigationCallback {
    void navigateToFragment(Fragment fragment, String tag);
    void popFragment();
    void popToRoot();
    void showBottomBar();
    void hideBottomBar();
}