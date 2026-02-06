package com.example.yumi.utils;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.yumi.data.config.SharedPreferencesKeysConfig;


public class SharedPreferencesHelper {

    private final SharedPreferences preferences;

    public SharedPreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(
                SharedPreferencesKeysConfig.PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void setOnboardingCompleted(boolean completed) {
        preferences.edit()
                .putBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, completed)
                .apply();
    }

    public boolean isOnboardingCompleted() {
        return preferences.getBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        preferences.edit()
                .putBoolean(SharedPreferencesKeysConfig.KEY_LOGGED_IN, loggedIn)
                .apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(SharedPreferencesKeysConfig.KEY_LOGGED_IN, false);
    }

    public void setUserId(String userId) {
        preferences.edit()
                .putString(SharedPreferencesKeysConfig.KEY_USER_ID, userId)
                .apply();
    }

    public String getUserId() {
        return preferences.getString(SharedPreferencesKeysConfig.KEY_USER_ID, null);
    }

    public void setDarkMode(boolean darkMode) {
        preferences.edit()
                .putBoolean(SharedPreferencesKeysConfig.KEY_DARK_MODE, darkMode)
                .apply();
    }

    public boolean isDarkMode() {
        return preferences.getBoolean(SharedPreferencesKeysConfig.KEY_DARK_MODE, false);
    }

    public void setLanguage(String language) {
        preferences.edit()
                .putString(SharedPreferencesKeysConfig.KEY_LANGUAGE, language)
                .apply();
    }

    public String getLanguage() {
        return preferences.getString(SharedPreferencesKeysConfig.KEY_LANGUAGE, "en");
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }

    public void clearUserData() {
        boolean onboardingCompleted = isOnboardingCompleted();
        preferences.edit()
                .clear()
                .putBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, onboardingCompleted)
                .apply();
    }
}