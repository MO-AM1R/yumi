package com.example.yumi.data.user.datasources.local;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.yumi.data.config.SharedPreferencesKeysConfig;
import com.example.yumi.data.firebase.UserSessionManager;
import com.example.yumi.domain.user.model.User;

public class UserLocalDataSourceImpl implements UserLocalDataSource {

    private final SharedPreferences preferences;
    private final UserSessionManager userSession;

    public UserLocalDataSourceImpl(Context context) {
        this.preferences = context.getSharedPreferences(
                SharedPreferencesKeysConfig.PREF_NAME,
                Context.MODE_PRIVATE
        );
        this.userSession = UserSessionManager.getInstance();
    }


    @Override
    public void setOnboardingCompleted(boolean completed) {
        preferences.edit()
                .putBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, completed)
                .apply();
    }

    @Override
    public boolean isOnboardingCompleted() {
        return preferences.getBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, false);
    }

    @Override
    public void setLoggedIn(boolean loggedIn) {
        preferences.edit()
                .putBoolean(SharedPreferencesKeysConfig.KEY_LOGGED_IN, loggedIn)
                .apply();
    }

    @Override
    public boolean isLoggedIn() {
        return preferences.getBoolean(SharedPreferencesKeysConfig.KEY_LOGGED_IN, false);
    }

    @Override
    public void setUserId(String userId) {
        preferences.edit()
                .putString(SharedPreferencesKeysConfig.KEY_USER_ID, userId)
                .apply();
    }

    @Override
    public String getUserId() {
        return preferences.getString(SharedPreferencesKeysConfig.KEY_USER_ID, null);
    }

    @Override
    public void setCurrentUser(User user) {
        userSession.setCurrentUser(user);
    }

    @Override
    public User getCurrentUser() {
        return userSession.getCurrentUser();
    }

    @Override
    public void clearSession() {
        userSession.clearSession();
    }

    @Override
    public void clearUserData() {
        boolean onboardingCompleted = isOnboardingCompleted();

        preferences.edit()
                .remove(SharedPreferencesKeysConfig.KEY_LOGGED_IN)
                .remove(SharedPreferencesKeysConfig.KEY_USER_ID)
                .putBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, onboardingCompleted)
                .apply();
    }
}