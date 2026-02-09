package com.example.yumi.data.favorite.repository;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import com.example.yumi.data.favorite.datasources.local.FavoritesLocalDataSource;
import com.example.yumi.data.favorite.datasources.local.FavoritesLocalDataSourceImpl;
import com.example.yumi.data.favorite.model.mapper.MealWithIngredientsMapper;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoritesLocalDataSource localDataSource;

    public FavoriteRepositoryImpl() {
        localDataSource = new FavoritesLocalDataSourceImpl();
    }

    @Override
    public Completable insertMeal(Meal meal) {
        return localDataSource.insertMeal(
                MealWithIngredientsMapper.toPojo(meal)
        );
    }

    @Override
    public Completable deleteMeal(String mealId) {
        return localDataSource.deleteMeal(mealId);
    }

    @Override
    public Completable clearAllMeals() {
        return localDataSource.clearAllMeals();
    }

    private List<Meal> mapToDomainList(List<MealWithIngredients> pojoList) {
        if (pojoList == null || pojoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Meal> meals = new ArrayList<>();
        for (MealWithIngredients pojo : pojoList) {
            Meal meal = MealWithIngredientsMapper.toDomain(pojo);
            if (meal != null) {
                meals.add(meal);
            }
        }

        return meals;
    }

    @Override
    public Flowable<List<Meal>> getAllFavoriteMeals() {
        return localDataSource.getAllFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::mapToDomainList);
    }

    @Override
    public Flowable<Boolean> isFav(String mealId) {
        return localDataSource.isFav(mealId);
    }
}
