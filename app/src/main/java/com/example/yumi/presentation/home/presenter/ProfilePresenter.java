package com.example.yumi.presentation.home.presenter;
import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.data.meals.repository.MealsRepositoryImpl;
import com.example.yumi.data.plan.repository.MealPlanRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.plan.models.MealPlan;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.base.BaseView;
import com.example.yumi.presentation.home.contract.ProfileContract;
import com.example.yumi.utils.LocaleHelper;
import com.example.yumi.utils.ThemeHelper;
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProfilePresenter extends BasePresenter<ProfileContract.View>
        implements ProfileContract.Presenter {
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final MealPlanRepository mealPlanRepository;
    private final MealsRepository mealsRepository;
    private final Context context;

    public ProfilePresenter(Context context) {
        this.context = context.getApplicationContext();
        this.userRepository = new UserRepositoryImpl(context);
        this.favoriteRepository = new FavoriteRepositoryImpl(context);
        this.mealPlanRepository = new MealPlanRepositoryImpl(context);
        this.mealsRepository = new MealsRepositoryImpl(context);
    }

    private void onUserDetailsLoaded(User user) {
        withView(v -> {
            v.hideLoading();
            v.showUserDetails(user);
        });
    }

    private void onFavoriteCounterLoaded(int counter) {
        withView(v -> v.showUserFavoriteMealsCounter(counter));
    }

    private void onPlannedMealsCounterLoaded(int counter) {
        withView(v -> v.showUserPlannedMealsCounter(counter));
    }

    @Override
    public void loadUserDetails() {
        withView(BaseView::showLoading);

        User user = userRepository.getCurrentUser();
        withView(view1 -> onUserDetailsLoaded(user));

        Disposable disposable1 = favoriteRepository.getAllFavoriteIds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        strings -> onFavoriteCounterLoaded(strings.size()),
                        throwable -> withView(v -> v.showError(throwable.getLocalizedMessage()))
                );

        Disposable disposable2 = mealPlanRepository.getAllPlannedMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        plans -> {
                            int totalCount = 0;
                            for (Map<MealType, Meal> day : plans.values()) {
                                if (day != null) totalCount += day.size();
                            }
                            onPlannedMealsCounterLoaded(totalCount);
                        },
                        throwable -> withView(v -> v.showError(throwable.getLocalizedMessage()))
                );

        compositeDisposable.add(disposable1);
        compositeDisposable.add(disposable2);
    }

    @Override
    public void onLanguageChanged(String lang) {
        if (LocaleHelper.isSameLanguage(context, lang)) return;
        LocaleHelper.saveLanguage(context, lang);
        withView(ProfileContract.View::resetLanguage);
    }

    @Override
    public void onModeChanged(boolean darkMode) {
        ThemeHelper.saveTheme(context, darkMode);
        AppCompatDelegate.setDefaultNightMode(
                darkMode
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    @Override
    public void syncData() {
        Disposable disposable = userRepository.syncData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> withView(ProfileContract.View::onLogout));

        compositeDisposable.add(disposable);
    }

    @Override
    public void logout() {
        Disposable disposable = userRepository.signOut()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> withView(ProfileContract.View::onLogout));

        compositeDisposable.add(disposable);
    }


    @Override
    public void retrieveData() {
        withView(ProfileContract.View::showLoading);

        Disposable disposable = userRepository.retrieveData()
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(this::syncUserData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> withView(v -> {
                            v.hideLoading();
                            v.onDataRetrievedSuccess();
                        }),
                        e -> withView(v -> {
                            v.hideLoading();
                            v.showError(e.getLocalizedMessage());
                        })
                );

        compositeDisposable.add(disposable);
    }

    private Completable syncUserData(User user) {
        return mealsRepository.clearAllLocalMeals()
                .andThen(syncFavorites(user.getFavoriteMealIds()))
                .andThen(syncMealPlan(user.getMealPlan()));
    }

    private Completable syncFavorites(List<String> favoriteIds) {
        return Observable.fromIterable(favoriteIds)
                .concatMapSingle(mealId ->
                        mealsRepository.getMealById(mealId)
                                .onErrorReturnItem(createEmptyMeal())
                )
                .filter(meal -> meal != null && meal.getId() != null && !meal.getId().isEmpty())
                .concatMapCompletable(favoriteRepository::insertMeal);
    }

    private Meal createEmptyMeal() {
        return new Meal("", "", "", "", "", "", "", new ArrayList<>(), "", "", new ArrayList<>());
    }

    private Completable syncMealPlan(MealPlan mealPlan) {
        Set<String> mealIds = collectMealIds(mealPlan);

        return fetchMeals(new ArrayList<>(mealIds))
                .flatMapCompletable(meals -> {
                    Map<String, Meal> mealMap = toMealMap(meals);
                    return insertMealPlan(mealPlan, mealMap);
                });
    }

    private Set<String> collectMealIds(MealPlan mealPlan) {
        Set<String> ids = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayAtMidnight = cal.getTime();

        for (Map.Entry<String, DayMeals> entry : mealPlan.getDays().entrySet()) {
            String dateStr = entry.getKey();
            DayMeals day = entry.getValue();

            try {
                Date planDate = sdf.parse(dateStr);

                if (day == null || planDate == null || planDate.before(todayAtMidnight)) {
                    continue;
                }

                add(ids, day.getBreakfast());
                add(ids, day.getLunch());
                add(ids, day.getDinner());
                add(ids, day.getSnack());

            } catch (ParseException e) {
                Log.e("ProfilePresenter", "Parsing error for date: " + dateStr);
            }
        }
        return ids;
    }

    private void add(Set<String> set, String id) {
        if (id != null && !id.isEmpty()) {
            set.add(id);
        }
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

            addMeal(actions, date, MealType.BREAKFAST, day.getBreakfast(), meals);
            addMeal(actions, date, MealType.LUNCH, day.getLunch(), meals);
            addMeal(actions, date, MealType.DINNER, day.getDinner(), meals);
            addMeal(actions, date, MealType.SNACK, day.getSnack(), meals);
        }

        return actions.isEmpty()
                ? Completable.complete()
                : Completable.merge(actions);
    }

    private void addMeal(
            List<Completable> actions,
            String date,
            MealType type,
            String mealId,
            Map<String, Meal> meals
    ) {
        Meal meal = meals.get(mealId);
        if (meal != null) {
            actions.add(mealPlanRepository.addMealToPlan(date, type, meal));
        }
    }

    private Map<String, Meal> toMealMap(List<Meal> meals) {
        Map<String, Meal> map = new HashMap<>();
        for (Meal meal : meals) {
            map.put(meal.getId(), meal);
        }
        return map;
    }
}
