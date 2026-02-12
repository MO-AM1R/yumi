package com.example.yumi.data.user.datasources.local;
import com.example.yumi.domain.user.model.User;


public interface UserLocalDataSource {

    void setOnboardingCompleted(boolean completed);

    boolean isOnboardingCompleted();

    void setLoggedIn(boolean loggedIn);

    boolean isLoggedIn();

    void setUserId(String userId);

    String getUserId();

    void setCurrentUser(User user);

    User getCurrentUser();

    void clearSession();

    void clearUserData();
}
