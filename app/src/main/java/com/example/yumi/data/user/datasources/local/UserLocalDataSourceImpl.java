package com.example.yumi.data.user.datasources.local;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.yumi.data.config.SharedPreferencesKeysConfig;
import com.example.yumi.data.firebase.UserSessionManager;
import com.example.yumi.domain.user.model.DayMeals;
import com.example.yumi.domain.user.model.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.model.UserSettings;
import java.util.List;

public class UserLocalDataSourceImpl implements UserLocalDataSource {

    private final SharedPreferences preferences;
    private final UserSessionManager userSession;

    public UserLocalDataSourceImpl(Context context) {
        this.preferences = context.getSharedPreferences(
                SharedPreferencesKeysConfig.PREF_NAME,
                Context.MODE_PRIVATE
        );
        this.userSession = new UserSessionManager();
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
    public void cacheSettings(UserSettings settings) {
        preferences.edit()
                .putBoolean(SharedPreferencesKeysConfig.KEY_DARK_MODE, settings.isDarkMode())
                .putString(SharedPreferencesKeysConfig.KEY_LANGUAGE, settings.getLanguage())
                .apply();
    }

    @Override
    public UserSettings getCachedSettings() {
        boolean darkMode = preferences.getBoolean(SharedPreferencesKeysConfig.KEY_DARK_MODE, false);
        String language = preferences.getString(SharedPreferencesKeysConfig.KEY_LANGUAGE, "en");
        return new UserSettings(darkMode, language);
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
    public boolean hasUser() {
        return userSession.hasUser();
    }

    @Override
    public void updateUserSettings(UserSettings settings) {
        userSession.updateSettings(settings);
    }

    @Override
    public UserSettings getUserSettings() {
        return userSession.getSettings();
    }

    @Override
    public void setFavoriteMealIds(List<String> mealIds) {
        userSession.setFavoriteMealIds(mealIds);
    }

    @Override
    public List<String> getFavoriteMealIds() {
        return userSession.getFavoriteMealIds();
    }

    @Override
    public void addFavoriteMeal(String mealId) {
        userSession.addFavoriteMeal(mealId);
    }

    @Override
    public void removeFavoriteMeal(String mealId) {
        userSession.removeFavoriteMeal(mealId);
    }

    @Override
    public boolean isFavorite(String mealId) {
        return userSession.isFavorite(mealId);
    }

    @Override
    public void setMealPlan(MealPlan mealPlan) {
        userSession.setMealPlan(mealPlan);
    }

    @Override
    public MealPlan getMealPlan() {
        return userSession.getMealPlan();
    }

    @Override
    public DayMeals getDayMeals(String date) {
        return userSession.getMealPlan().getDayMeals(date);
    }

    @Override
    public void setDayMeals(String date, DayMeals dayMeals) {
        userSession.getMealPlan().setDayMeals(date, dayMeals);
    }

    @Override
    public void setMealForDay(String date, MealType mealType, String mealId) {
        userSession.setMealForDay(date, mealType, mealId);
    }

    @Override
    public void removeMealFromDay(String date, MealType mealType) {
        userSession.removeMealFromDay(date, mealType);
    }


    @Override
    public void clearSession() {
        userSession.clearSession();
    }

    @Override
    public void clearUserData() {
        boolean onboardingCompleted = isOnboardingCompleted();

        preferences.edit()
                .clear()
                .putBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, onboardingCompleted)
                .apply();
    }

    @Override
    public void clearAll() {
        preferences.edit().clear().apply();
        userSession.clearSession();
    }
}