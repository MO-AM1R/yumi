package com.example.yumi.data.meals.datasources.local;
import android.content.Context;
import com.example.yumi.data.database.AppDatabase;
import com.example.yumi.data.database.doa.MealDao;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource {
    private final MealDao mealDao;

    public MealsLocalDataSourceImpl(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        mealDao = db.mealDao();
    }

    @Override
    public Maybe<MealWithIngredients> getMealById(String mealId) {
        return mealDao.getMealById(mealId);
    }

    @Override
    public Single<Boolean> mealExists(String mealId) {
        return mealDao.mealExists(mealId);
    }

    @Override
    public Completable clearAllMeals() {
        return mealDao.clearAllMeals();
    }
}