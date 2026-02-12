package com.example.yumi.data.user.datasources.local;
import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.plan.models.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.model.UserSettings;
import java.util.List;

public interface UserLocalDataSource {

    void setOnboardingCompleted(boolean completed);

    boolean isOnboardingCompleted();

    void setLoggedIn(boolean loggedIn);

    boolean isLoggedIn();

    void setUserId(String userId);

    String getUserId();

    void cacheSettings(UserSettings settings);

    UserSettings getCachedSettings();

    void setCurrentUser(User user);

    User getCurrentUser();

    boolean hasUser();

    void setFavoriteMealIds(List<String> mealIds);

    List<String> getFavoriteMealIds();

    void addFavoriteMeal(String mealId);

    void removeFavoriteMeal(String mealId);

    boolean isFavorite(String mealId);

    void setMealPlan(MealPlan mealPlan);

    MealPlan getMealPlan();

    DayMeals getDayMeals(String date);

    void setDayMeals(String date, DayMeals dayMeals);

    void setMealForDay(String date, MealType mealType, String mealId);

    void removeMealFromDay(String date, MealType mealType);

    void clearSession();

    void clearUserData();

    void clearAll();
}
