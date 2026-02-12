package com.example.yumi.data.meals.repository;
import android.content.Context;
import android.util.Log;

import com.example.yumi.data.database.mapper.MealWithIngredientsMapper;
import com.example.yumi.data.meals.datasources.local.MealsLocalDataSource;
import com.example.yumi.data.meals.datasources.local.MealsLocalDataSourceImpl;
import com.example.yumi.data.meals.datasources.remote.MealsRemoteDataSource;
import com.example.yumi.data.meals.datasources.remote.MealsRemoteDataSourceImpl;
import com.example.yumi.data.meals.model.mapper.AreaMapper;
import com.example.yumi.data.meals.model.mapper.CategoryMapper;
import com.example.yumi.data.meals.model.mapper.IngredientMapper;
import com.example.yumi.data.meals.model.mapper.MealMapper;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import com.example.yumi.domain.plan.models.MealPlan;
import java.util.Collections;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;


public class MealsRepositoryImpl implements MealsRepository {
    private final MealsRemoteDataSource remoteDataSource;
    private final MealsLocalDataSource localDataSource;

    public MealsRepositoryImpl() {
        this.remoteDataSource = new MealsRemoteDataSourceImpl();
        this.localDataSource = null;
    }

    public MealsRepositoryImpl(Context context) {
        this.remoteDataSource = new MealsRemoteDataSourceImpl();
        this.localDataSource = new MealsLocalDataSourceImpl(context);
    }

    @Override
    public Single<Meal> getDayMeal() {
        return remoteDataSource.geDayMeal().map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return null;
            }
            return MealMapper.mapToDomain(mealsResponse.getMeals().get(0));
        });
    }

    @Override
    public Single<List<Meal>> getRandomMeals(String letter) {
        return remoteDataSource.searchMealsByFirstLetter(letter).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return Collections.emptyList();
            }
            return MealMapper.mapToDomainList(mealsResponse.getMeals());
        });
    }

    @Override
    public Single<Meal> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return null;
            }
            return MealMapper.mapToDomain(mealsResponse.getMeals().get(0));
        });
    }

    @Override
    public Single<List<Meal>> searchMealsByName(String mealName) {
        return remoteDataSource.searchMealsByName(mealName).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return Collections.emptyList();
            }
            return MealMapper.mapToDomainList(mealsResponse.getMeals());
        });
    }

    @Override
    public Single<List<Meal>> searchMealsByFirstLetter(String firstLetter) {
        return remoteDataSource.searchMealsByFirstLetter(firstLetter).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return Collections.emptyList();
            }
            return MealMapper.mapToDomainList(mealsResponse.getMeals());
        });
    }

    @Override
    public Single<List<Meal>> filterMealsByCategory(String category) {
        return remoteDataSource.filterMealsByCategory(category).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return Collections.emptyList();
            }
            return MealMapper.mapToDomainList(mealsResponse.getMeals());
        });
    }

    @Override
    public Single<List<Meal>> filterMealsByArea(String area) {
        return remoteDataSource.filterMealsByArea(area).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return Collections.emptyList();
            }
            return MealMapper.mapToDomainList(mealsResponse.getMeals());
        });
    }

    @Override
    public Single<List<Meal>> filterMealsByIngredient(String ingredient) {
        return remoteDataSource.filterMealsByIngredient(ingredient).map(mealsResponse -> {
            if (mealsResponse.getMeals() == null || mealsResponse.getMeals().isEmpty()) {
                return Collections.emptyList();
            }
            return MealMapper.mapToDomainList(mealsResponse.getMeals());
        });
    }

    @Override
    public Single<List<Category>> getAllCategories() {
        return remoteDataSource.getAllCategories().map(mealsResponse -> {
            if (mealsResponse.getCategories() == null || mealsResponse.getCategories().isEmpty()) {
                return Collections.emptyList();
            }
            return CategoryMapper.mapToDomainList(mealsResponse.getCategories());
        });
    }

    @Override
    public Single<List<Area>> getAllAreas() {
        return remoteDataSource.getAllAreas().map(mealsResponse -> {
            if (mealsResponse.getAreas() == null || mealsResponse.getAreas().isEmpty()) {
                return Collections.emptyList();
            }
            return AreaMapper.mapToDomainList(mealsResponse.getAreas());
        });
    }

    @Override
    public Single<List<Ingredient>> getAllIngredients() {
        return remoteDataSource.getAllIngredients().map(mealsResponse -> {
            if (mealsResponse.getIngredients() == null || mealsResponse.getIngredients().isEmpty()) {
                return Collections.emptyList();
            }
            return IngredientMapper.mapToDomainList(mealsResponse.getIngredients());
        });
    }

    @Override
    public Maybe<Meal> getLocalMealById(String mealId) {
        if (localDataSource == null) {
            return Maybe.empty();
        }
        return localDataSource.getMealById(mealId)
                .map(MealWithIngredientsMapper::toDomain);
    }

    @Override
    public Single<Boolean> mealExistsLocally(String mealId) {
        if (localDataSource == null) {
            return Single.just(false);
        }
        return localDataSource.mealExists(mealId);
    }

    @Override
    public Completable clearAllLocalMeals() {
        if (localDataSource == null) {
            return Completable.complete();
        }
        return localDataSource.clearAllMeals();
    }

    @Override
    public Completable addToMealPlan(MealPlan mealPlan) {
        // TODO: Implement when needed
        return Completable.complete();
    }
}