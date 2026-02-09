package com.example.yumi.data.favorite.datasources.local;
import android.content.Context;

import com.example.yumi.data.database.AppDatabase;
import com.example.yumi.data.database.doa.IngredientDao;
import com.example.yumi.data.database.doa.MealDao;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public class FavoritesLocalDataSourceImpl implements FavoritesLocalDataSource {
    private final MealDao mealDao;
    private final IngredientDao ingredientDao;

    public FavoritesLocalDataSourceImpl(Context context) {
        mealDao = AppDatabase.getInstance(context).mealDao();
        ingredientDao = AppDatabase.getInstance(context).ingredientDao();
    }

    @Override
    public Completable insertMeal(MealWithIngredients meal) {
        return mealDao.insertMeal(meal.getMeal())
                .andThen(
                        ingredientDao.insertIngredients(meal.getIngredients())
                );
    }

    @Override
    public Completable deleteMeal(String mealId) {
        return mealDao.deleteMeal(mealId);
    }

    @Override
    public Completable clearAllMeals() {
        return mealDao.clearAllMeals();
    }

    @Override
    public Flowable<List<MealWithIngredients>> getAllFavoriteMeals() {
        return mealDao.getAllFavoriteMeals();
    }

    @Override
    public Flowable<Boolean> isFav(String mealId) {
        return mealDao.isFav(mealId);
    }
}