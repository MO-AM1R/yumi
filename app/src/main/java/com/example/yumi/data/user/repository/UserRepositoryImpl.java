package com.example.yumi.data.user.repository;
import com.example.yumi.data.firebase.FirebaseModule;
import com.example.yumi.data.user.datasources.local.UserLocalDataSource;
import com.example.yumi.data.user.datasources.local.UserLocalDataSourceImpl;
import com.example.yumi.data.user.datasources.remote.UserRemoteDataSource;
import com.example.yumi.data.user.datasources.remote.UserRemoteDataSourceImpl;
import com.example.yumi.domain.user.repository.UserRepository;
import android.content.Context;
import com.example.yumi.domain.user.model.DayMeals;
import com.example.yumi.domain.user.model.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.model.UserSettings;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class UserRepositoryImpl implements UserRepository {
    private final UserRemoteDataSource remoteDataSource;
    private final UserLocalDataSource userLocalDataSource;

    public UserRepositoryImpl(Context context) {
        FirebaseModule module = FirebaseModule.getInstance();
        this.remoteDataSource = new UserRemoteDataSourceImpl(
                module.getAuthService(),
                module.getFirestoreService()
        );
        this.userLocalDataSource = new UserLocalDataSourceImpl(
                context
        );
    }


    @Override
    public Single<User> signInWithEmail(String email, String password) {
        return remoteDataSource.signInWithEmail(email, password)
                .flatMap(this::handleAuthSuccess);
    }

    @Override
    public Single<User> signUpWithEmail(String email, String password, String displayName) {
        return remoteDataSource.signUpWithEmail(email, password, displayName)
                .flatMap(user -> remoteDataSource.createUser(user)
                        .andThen(Single.just(user))
                        .onErrorResumeNext(e -> Single.just(user)))
                .flatMap(this::handleAuthSuccess);
    }

    @Override
    public Single<User> signInWithGoogle(Context context) {
        return remoteDataSource.signInWithGoogle(context)
                .flatMap(user -> remoteDataSource.createUser(user)
                        .andThen(Single.just(user))
                        .onErrorResumeNext(e -> Single.just(user)))
                .flatMap(this::handleAuthSuccess);
    }

    @Override
    public Completable signOut() {
        return remoteDataSource.signOut()
                .doOnComplete(() -> {
                    userLocalDataSource.clearSession();
                    userLocalDataSource.clearUserData();
                });
    }

    @Override
    public User getCurrentUser() {
        User cachedUser = userLocalDataSource.getCurrentUser();
        if (cachedUser != null) {
            return cachedUser;
        }
        return remoteDataSource.getCurrentAuthUser();
    }

    @Override
    public boolean isLoggedIn() {
        return userLocalDataSource.isLoggedIn() && remoteDataSource.isUserLoggedIn();
    }

    @Override
    public String getCurrentUserId() {
        return remoteDataSource.getCurrentUserId();
    }

    @Override
    public void setOnboardingCompleted(boolean completed) {
        userLocalDataSource.setOnboardingCompleted(completed);
    }

    @Override
    public boolean isOnboardingCompleted() {
        return userLocalDataSource.isOnboardingCompleted();
    }

    @Override
    public Single<UserSettings> getUserSettings() {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Single.error(new Exception("User not logged in"));
        }

        return remoteDataSource.getUserSettings(userId)
                .doOnSuccess(settings -> {
                    userLocalDataSource.cacheSettings(settings);
                    userLocalDataSource.updateUserSettings(settings);
                })
                .onErrorResumeNext(e -> Single.just(userLocalDataSource.getCachedSettings()));
    }

    @Override
    public Completable updateUserSettings(UserSettings settings) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        userLocalDataSource.cacheSettings(settings);
        userLocalDataSource.updateUserSettings(settings);

        return remoteDataSource.updateUserSettings(userId, settings);
    }

    @Override
    public Single<List<String>> getFavoriteMeals() {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Single.error(new Exception("User not logged in"));
        }

        return remoteDataSource.getFavoriteMeals(userId)
                .doOnSuccess(userLocalDataSource::setFavoriteMealIds);
    }

    @Override
    public Completable addFavoriteMeal(String mealId) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        userLocalDataSource.addFavoriteMeal(mealId);

        return remoteDataSource.addFavoriteMeal(userId, mealId)
                .doOnError(e -> userLocalDataSource.removeFavoriteMeal(mealId));
    }

    @Override
    public Completable removeFavoriteMeal(String mealId) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        userLocalDataSource.removeFavoriteMeal(mealId);

        return remoteDataSource.removeFavoriteMeal(userId, mealId)
                .doOnError(e -> userLocalDataSource.addFavoriteMeal(mealId));
    }

    @Override
    public boolean isFavorite(String mealId) {
        return userLocalDataSource.isFavorite(mealId);
    }

    @Override
    public Single<MealPlan> getMealPlan() {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Single.error(new Exception("User not logged in"));
        }

        return remoteDataSource.getMealPlan(userId)
                .doOnSuccess(userLocalDataSource::setMealPlan);
    }

    @Override
    public Single<DayMeals> getDayMeals(String date) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Single.error(new Exception("User not logged in"));
        }

        DayMeals cachedDay = userLocalDataSource.getDayMeals(date);
        if (cachedDay != null) {
            return Single.just(cachedDay);
        }

        return remoteDataSource.getDayMeals(userId, date)
                .doOnSuccess(dayMeals -> userLocalDataSource.setDayMeals(date, dayMeals));
    }

    @Override
    public Completable setDayMeals(String date, DayMeals dayMeals) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        userLocalDataSource.setDayMeals(date, dayMeals);
        return remoteDataSource.setDayMeals(userId, date, dayMeals);
    }

    @Override
    public Completable setMealForDay(String date, MealType mealType, String mealId) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        userLocalDataSource.setMealForDay(date, mealType, mealId);
        return remoteDataSource.setMealForDay(userId, date, mealType, mealId);
    }

    @Override
    public Completable removeMealFromDay(String date, MealType mealType) {
        String userId = remoteDataSource.getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        userLocalDataSource.removeMealFromDay(date, mealType);
        return remoteDataSource.removeMealFromDay(userId, date, mealType);
    }

    private Single<User> handleAuthSuccess(User user) {
        userLocalDataSource.setLoggedIn(true);
        userLocalDataSource.setUserId(user.getUid());

        return remoteDataSource.getUser(user.getUid())
                .doOnSuccess(fullUser -> {
                    userLocalDataSource.setCurrentUser(fullUser);

                    if (fullUser.getSettings() != null) {
                        userLocalDataSource.cacheSettings(fullUser.getSettings());
                    }
                })
                .onErrorResumeNext(e -> {
                    userLocalDataSource.setCurrentUser(user);
                    return Single.just(user);
                });
    }
}
