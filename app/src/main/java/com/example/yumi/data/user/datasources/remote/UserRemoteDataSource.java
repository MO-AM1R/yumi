package com.example.yumi.data.user.datasources.remote;
import android.content.Context;
import com.example.yumi.domain.user.model.DayMeals;
import com.example.yumi.domain.user.model.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.model.UserSettings;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public interface UserRemoteDataSource {
    Single<User> signInWithEmail(String email, String password);
    Single<User> signUpWithEmail(String email, String password, String displayName);

    Single<User> signInWithGoogle(Context context);

    Completable signOut();
    User getCurrentAuthUser();
    boolean isUserLoggedIn();
    String getCurrentUserId();

    Completable createUser(User user);
    Single<User> getUser(String userId);
    Completable deleteUser(String userId);

    Single<UserSettings> getUserSettings(String userId);
    Completable updateUserSettings(String userId, UserSettings settings);

    Single<List<String>> getFavoriteMeals(String userId);
    Completable addFavoriteMeal(String userId, String mealId);
    Completable removeFavoriteMeal(String userId, String mealId);
    Completable setFavoriteMeals(String userId, List<String> mealIds);

    Single<MealPlan> getMealPlan(String userId);
    Single<DayMeals> getDayMeals(String userId, String date);
    Completable setDayMeals(String userId, String date, DayMeals dayMeals);
    Completable removeDayMeals(String userId, String date);
    Completable setMealForDay(String userId, String date, MealType mealType, String mealId);
    Completable removeMealFromDay(String userId, String date, MealType mealType);
}
