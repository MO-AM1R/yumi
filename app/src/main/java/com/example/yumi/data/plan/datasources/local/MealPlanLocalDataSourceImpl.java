package com.example.yumi.data.plan.datasources.local;
import android.content.Context;
import com.example.yumi.data.database.AppDatabase;
import com.example.yumi.data.database.doa.IngredientDao;
import com.example.yumi.data.database.doa.MealDao;
import com.example.yumi.data.database.doa.MealPlanDao;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.entity.MealIngredientEntity;
import com.example.yumi.data.database.entity.MealPlanEntity;
import com.example.yumi.data.database.pojo.PlanEntryWithMeal;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealIngredient;
import com.example.yumi.domain.user.model.MealType;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public class MealPlanLocalDataSourceImpl implements MealPlanLocalDataSource {
    private final MealDao mealDao;
    private final IngredientDao ingredientDao;
    private final MealPlanDao mealPlanDao;

    public MealPlanLocalDataSourceImpl(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.mealDao = db.mealDao();
        this.ingredientDao = db.ingredientDao();
        this.mealPlanDao = db.mealPlanDao();
    }

    @Override
    public Completable addMealToPlan(String date, MealType mealType, Meal meal) {
        MealEntity mealEntity = new MealEntity(
                meal.getId(),
                meal.getName(),
                meal.getCategory(),
                meal.getArea(),
                meal.getInstructions(),
                meal.getThumbnailUrl(),
                meal.getYoutubeUrl()
        );

        List<MealIngredientEntity> ingredientEntities = new ArrayList<>();
        for (MealIngredient mealIngredient: meal.getIngredients()){
            ingredientEntities.add(new MealIngredientEntity(
                    mealIngredient.getName(),
                    meal.getId(),
                    mealIngredient.getMeasure(),
                    mealIngredient.getThumbnailUrl()
            ));
        }
        MealPlanEntity planEntity = new MealPlanEntity(date, mealType.name(), meal.getId());

        return mealDao.insertMeal(mealEntity)
                .andThen(ingredientDao.insertIngredients(ingredientEntities))
                .andThen(mealPlanDao.insertPlanEntry(planEntity));
    }

    private Completable deleteMealIfOrphan(String mealId) {
        return mealDao.isMealOrphan(mealId)
                .flatMapCompletable(isOrphan -> {
                    if (isOrphan) {
                        return mealDao.deleteMeal(mealId);
                    }
                    return Completable.complete();
                });
    }

    @Override
    public Completable removeMealFromPlan(String date, MealType mealType) {
        return mealPlanDao.getMealIdForPlanEntry(date, mealType.name())
                .flatMapCompletable(mealId ->
                        mealPlanDao.deletePlanEntry(date, mealType.name())
                                .andThen(deleteMealIfOrphan(mealId))
                )
                .onErrorComplete();
    }

    @Override
    public Flowable<List<PlanEntryWithMeal>> getMealsForDate(String date) {
        return mealPlanDao.getPlanEntriesForDate(date);
    }

    @Override
    public Flowable<List<PlanEntryWithMeal>> getAllPlanEntries() {
        return mealPlanDao.getAllPlanEntries();
    }

    @Override
    public Flowable<Boolean> hasMealForDateAndType(String date, MealType mealType) {
        return mealPlanDao.hasPlanEntry(date, mealType.name());
    }

    @Override
    public Completable deletePlanEntriesNotInDates(List<String> validDates) {
        return mealPlanDao.deletePlanEntriesNotInDates(validDates);
    }

    @Override
    public Completable clearAllPlanEntries() {
        return mealPlanDao.clearAllPlanEntries();
    }
}