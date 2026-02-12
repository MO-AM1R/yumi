package com.example.yumi.presentation.home.contract;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.presentation.base.BaseView;


public interface ProfileContract {
    interface View extends BaseView {
        void showUserDetails(User user);
        void showUserPlannedMealsCounter(int plannedMeals);
        void showUserFavoriteMealsCounter(int favoriteMeals);
        void resetLanguage();
        void onLogout();

        void onDataRetrievedSuccess();
    }

    interface Presenter {
        void loadUserDetails();
        void onLanguageChanged(String lang);
        void onModeChanged(boolean isDark);
        void syncData();
        void retrieveData();
        void logout();
    }
}
