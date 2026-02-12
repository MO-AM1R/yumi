package com.example.yumi.presentation.authentication.presenter;
import static com.example.yumi.data.config.AppConfigurations.DATE_FORMAT;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.data.plan.repository.MealPlanRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.base.BasePresenter;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.plan.models.MealPlan;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


public class LoginPresenter extends BasePresenter<AuthContract.LoginView>
        implements AuthContract.LoginPresenter {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final MealsRepository mealsRepository;
    private final MealPlanRepository mealPlanRepository;
    private final AuthContract.LoginView view;

    public LoginPresenter(Application application, AuthContract.LoginView view) {
        this.userRepository = new UserRepositoryImpl(application.getApplicationContext());
        this.favoriteRepository = new FavoriteRepositoryImpl(application.getApplicationContext());
        this.mealsRepository = new MealsRepositoryImpl(application.getApplicationContext());
        this.mealPlanRepository = new MealPlanRepositoryImpl(application.getApplicationContext());
        this.view = view;
    }

    @Override
    public void login(String email, String password) {
        if (isViewDetached()) return;
        view.clearErrors();
        if (!validateInput(email, password))
            return;

        view.showLoading();

        compositeDisposable.add(
                userRepository.signInWithEmail(email, password)
                        .subscribeOn(Schedulers.io())
                        .flatMap(user -> userRepository.retrieveData())
                        .flatMapCompletable(this::syncUserData)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.onLoginSuccess();
                                    }
                                },
                                throwable -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.showError("Login/Sync failed: " + throwable.getLocalizedMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void loginWithGoogle(Context context) {
        if (isViewDetached()) return;
        view.showLoading();

        compositeDisposable.add(
                userRepository.signInWithGoogle(context)
                        .subscribeOn(Schedulers.io())
                        .flatMap(user -> userRepository.retrieveData())
                        .flatMapCompletable(this::syncUserData)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.onLoginSuccess();
                                    }
                                },
                                throwable -> {
                                    if (!isViewDetached()) {
                                        view.hideLoading();
                                        view.showError("Google Login failed: " + throwable);
                                    }
                                }
                        )
        );
    }

    private Completable syncUserData(User user) {
        return mealsRepository.clearAllLocalMeals()
                .andThen(syncFavorites(user.getFavoriteMealIds()))
                .andThen(syncMealPlan(user.getMealPlan()));
    }

    private Completable syncFavorites(List<String> favoriteIds) {
        if (favoriteIds == null || favoriteIds.isEmpty()) return Completable.complete();
        return Observable.fromIterable(favoriteIds)
                .concatMapSingle(mealId ->
                        mealsRepository.getMealById(mealId)
                                .onErrorReturnItem(createEmptyMeal())
                )
                .filter(meal -> meal != null && meal.getId() != null && !meal.getId().isEmpty())
                .concatMapCompletable(favoriteRepository::insertMeal);
    }

    private Completable syncMealPlan(MealPlan mealPlan) {
        if (mealPlan == null || mealPlan.getDays() == null) return Completable.complete();
        Set<String> mealIds = collectMealIds(mealPlan);

        return fetchMeals(new ArrayList<>(mealIds))
                .flatMapCompletable(meals -> {
                    Map<String, Meal> mealMap = toMealMap(meals);
                    return insertMealPlan(mealPlan, mealMap);
                });
    }

    private Set<String> collectMealIds(MealPlan mealPlan) {
        Set<String> ids = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date todayAtMidnight = getTodayAtMidnight();

        for (Map.Entry<String, DayMeals> entry : mealPlan.getDays().entrySet()) {
            String dateStr = entry.getKey();
            DayMeals day = entry.getValue();
            try {
                Date planDate = sdf.parse(dateStr);
                if (day == null || planDate == null || planDate.before(todayAtMidnight)) continue;

                addId(ids, day.getBreakfast());
                addId(ids, day.getLunch());
                addId(ids, day.getDinner());
                addId(ids, day.getSnack());
            } catch (ParseException ignored) {

            }
        }
        return ids;
    }

    private void addId(Set<String> set, String id) {
        if (id != null && !id.isEmpty()) set.add(id);
    }

    private Single<List<Meal>> fetchMeals(List<String> ids) {
        return Observable.fromIterable(ids)
                .flatMapSingle(mealsRepository::getMealById)
                .toList();
    }

    private Completable insertMealPlan(MealPlan plan, Map<String, Meal> meals) {
        List<Completable> actions = new ArrayList<>();
        for (Map.Entry<String, DayMeals> entry : plan.getDays().entrySet()) {
            String date = entry.getKey();
            DayMeals day = entry.getValue();
            if (day == null) continue;

            addMealAction(actions, date, MealType.BREAKFAST, day.getBreakfast(), meals);
            addMealAction(actions, date, MealType.LUNCH, day.getLunch(), meals);
            addMealAction(actions, date, MealType.DINNER, day.getDinner(), meals);
            addMealAction(actions, date, MealType.SNACK, day.getSnack(), meals);
        }
        return actions.isEmpty() ? Completable.complete() : Completable.merge(actions);
    }

    private void addMealAction(List<Completable> actions, String date, MealType type, String mealId, Map<String, Meal> meals) {
        Meal meal = meals.get(mealId);
        if (meal != null) actions.add(mealPlanRepository.addMealToPlan(date, type, meal));
    }

    private Map<String, Meal> toMealMap(List<Meal> meals) {
        Map<String, Meal> map = new HashMap<>();
        for (Meal m : meals) map.put(m.getId(), m);
        return map;
    }

    private Meal createEmptyMeal() {
        return new Meal("", "", "", "", "", "", "", new ArrayList<>(), "", "", new ArrayList<>());
    }

    private Date getTodayAtMidnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private boolean validateInput(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            view.showEmailError("Email is required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showEmailError("Invalid email address");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            view.showPasswordError("Password is required");
            valid = false;
        } else if (password.length() < 6) {
            view.showPasswordError("Password must be at least 6 characters");
            valid = false;
        }

        return valid;
    }
}