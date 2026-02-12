package com.example.yumi.presentation.home.presenter;
import android.content.Context;
import com.example.yumi.data.config.AppConfigurations;
import com.example.yumi.data.plan.repository.MealPlanRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.plan.models.PlanDay;
import com.example.yumi.domain.plan.repository.MealPlanRepository;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.home.contract.MealPlanContract;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealPlanPresenter implements MealPlanContract.Presenter {
    private MealPlanContract.View view;
    private final MealPlanRepository repository;
    private final UserRepository userRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final List<PlanDay> days = new ArrayList<>();
    private int selectedDayPosition = 0;
    private Map<String, Map<MealType, Meal>> cachedMealPlan = new HashMap<>();
    private final SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
    private final SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.getDefault());
    private final SimpleDateFormat dateKeyFormat = new SimpleDateFormat(AppConfigurations.DATE_FORMAT, Locale.getDefault());

    {
        dateKeyFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public MealPlanPresenter(Context context, MealPlanContract.View view) {
        this.view = view;
        this.repository = new MealPlanRepositoryImpl(context);
        this.userRepository = new UserRepositoryImpl(context);
    }

    @Override
    public void loadMealPlan() {
        if (view == null) return;

        view.showLoading();

        generateDays();
        view.showDays(days);
        selectDay(0);
        cleanupOldDays();
    }

    private void generateDays() {
        days.clear();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();

            String dayName = dayNameFormat.format(date);
            int dayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            String fullDate = fullDateFormat.format(date);
            String dateKey = dateKeyFormat.format(date);
            boolean isSelected = (i == 0);

            PlanDay planDay = new PlanDay(date, dayName, dayNumber, fullDate, dateKey, isSelected);
            days.add(planDay);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void selectDay(int position) {
        if (position < 0 || position >= days.size()) return;

        int previousPosition = selectedDayPosition;
        selectedDayPosition = position;

        for (int i = 0; i < days.size(); i++) {
            days.get(i).setSelected(i == position);
        }

        if (view != null) {
            view.updateDaySelection(previousPosition, position);
            view.showSelectedDate(days.get(position).getFullDate());
        }

        loadMealsForSelectedDay();
    }

    private void loadMealsForSelectedDay() {
        if (view == null || days.isEmpty()) return;

        PlanDay selectedDay = days.get(selectedDayPosition);
        String dateKey = selectedDay.getDateKey();

        if (cachedMealPlan.containsKey(dateKey)) {
            displayDayMeals(cachedMealPlan.get(dateKey));
        } else {
            loadMealsForDateFromRepository(dateKey);
        }
    }

    private void displayDayMeals(Map<MealType, Meal> dayMeals) {
        if (view == null) return;

        // Breakfast
        if (dayMeals != null && dayMeals.containsKey(MealType.BREAKFAST)) {
            view.showMealForType(MealType.BREAKFAST, dayMeals.get(MealType.BREAKFAST));
        } else {
            view.showEmptyMealType(MealType.BREAKFAST);
        }

        // Lunch
        if (dayMeals != null && dayMeals.containsKey(MealType.LUNCH)) {
            view.showMealForType(MealType.LUNCH, dayMeals.get(MealType.LUNCH));
        } else {
            view.showEmptyMealType(MealType.LUNCH);
        }

        // Dinner
        if (dayMeals != null && dayMeals.containsKey(MealType.DINNER)) {
            view.showMealForType(MealType.DINNER, dayMeals.get(MealType.DINNER));
        } else {
            view.showEmptyMealType(MealType.DINNER);
        }

        // Snack
        if (dayMeals != null && dayMeals.containsKey(MealType.SNACK)) {
            view.showMealForType(MealType.SNACK, dayMeals.get(MealType.SNACK));
        } else {
            view.showEmptyMealType(MealType.SNACK);
        }
    }

    private void loadMealsForDateFromRepository(String dateKey) {
        Disposable disposable = repository.getMealsForDate(dateKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dayMeals -> {
                            cachedMealPlan.put(dateKey, dayMeals);
                            displayDayMeals(dayMeals);
                            if (view != null) {
                                view.hideLoading();
                            }
                        },
                        throwable -> {
                            displayDayMeals(null);
                            if (view != null) {
                                view.hideLoading();
                                view.showError(throwable.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    private void loadAllMealsFromRepository() {
        Disposable disposable = repository.getAllPlannedMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        allMeals -> {
                            cachedMealPlan = allMeals;
                            loadMealsForSelectedDay();
                            if (view != null) {
                                view.hideLoading();
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError(throwable.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void addMealToPlan(String dateKey, MealType mealType, Meal meal) {
        Disposable disposable = repository.addMealToPlan(dateKey, mealType, meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (!cachedMealPlan.containsKey(dateKey)) {
                                cachedMealPlan.put(dateKey, new HashMap<>());
                            }
                            Objects.requireNonNull(cachedMealPlan.get(dateKey)).put(mealType, meal);

                            PlanDay selectedDay = days.get(selectedDayPosition);
                            if (selectedDay.getDateKey().equals(dateKey)) {
                                if (view != null) {
                                    view.showMealForType(mealType, meal);
                                }
                            }

                            if (view != null) {
                                view.showMealAddedSuccess();
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.showError(throwable.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    private void removeMealFromPlan(String dateKey, MealType mealType) {
        Disposable disposable = repository.removeMealFromPlan(dateKey, mealType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (cachedMealPlan.containsKey(dateKey)) {
                                Objects.requireNonNull(cachedMealPlan.get(dateKey)).remove(mealType);
                            }

                            if (view != null) {
                                view.showEmptyMealType(mealType);
                                view.showMealRemovedSuccess();
                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.showError(throwable.getMessage());
                            }
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void cleanupOldDays() {
        String todayKey = dateKeyFormat.format(new Date());

        Disposable disposable = repository.cleanupOldDays(todayKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::loadAllMealsFromRepository,
                        throwable -> loadAllMealsFromRepository()
                );
        disposables.add(disposable);
    }

    @Override
    public void onDaySelected(int position) {
        if (position != selectedDayPosition) {
            selectDay(position);
        }
    }

    @Override
    public void onAddMealClicked(MealType mealType) {
        if (view == null || days.isEmpty()) return;

        PlanDay selectedDay = days.get(selectedDayPosition);
        view.navigateToAddMeal(selectedDay.getDateKey(), mealType);
    }

    @Override
    public void onRemoveMealClicked(MealType mealType) {
        if (days.isEmpty()) return;

        PlanDay selectedDay = days.get(selectedDayPosition);
        removeMealFromPlan(selectedDay.getDateKey(), mealType);
    }

    public String getSelectedDateKey() {
        if (selectedDayPosition >= 0 && selectedDayPosition < days.size()) {
            return days.get(selectedDayPosition).getDateKey();
        }
        return dateKeyFormat.format(new Date());
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        view = null;
    }

    @Override
    public boolean isGuestMode(){
        return userRepository.getCurrentUser().getUid().equalsIgnoreCase("Guest");
    }
}