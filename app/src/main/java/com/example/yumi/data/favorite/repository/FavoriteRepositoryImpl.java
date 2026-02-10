package com.example.yumi.data.favorite.repository;
import android.content.Context;
import com.example.yumi.data.database.mapper.MealWithIngredientsMapper;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import com.example.yumi.data.favorite.datasources.local.FavoritesLocalDataSource;
import com.example.yumi.data.favorite.datasources.local.FavoritesLocalDataSourceImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.meals.model.Meal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final FavoritesLocalDataSource localDataSource;

    public FavoriteRepositoryImpl(Context context) {
        localDataSource = new FavoritesLocalDataSourceImpl(context);
    }

    @Override
    public Completable insertMeal(Meal meal) {
        return localDataSource.addFavorite(
                MealWithIngredientsMapper.toPojo(meal)
        );
    }

    @Override
    public Completable deleteMeal(String mealId) {
        return localDataSource.removeFavorite(mealId);
    }

    public Single<Boolean> onFavMeal(Meal meal) {
        return isFav(meal.getId())
                .firstOrError()
                .flatMap(isFavorite -> {
                            if (isFavorite) {
                                return deleteMeal(meal.getId())
                                        .toSingleDefault(false);
                            } else {
                                return insertMeal(meal)
                                        .toSingleDefault(true);
                            }
                        }
                );
    }

    @Override
    public Completable clearAllFavorites() {
        return localDataSource.clearAllFavorites();
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
        return localDataSource.getAllFavorites()
                .map(this::mapToDomainList);
    }

    @Override
    public Flowable<Boolean> isFav(String mealId) {
        return localDataSource.isFavorite(mealId);
    }

    @Override
    public Flowable<Set<String>> getAllFavoriteIds() {
        return localDataSource.getAllFavoriteIds();
    }
}
