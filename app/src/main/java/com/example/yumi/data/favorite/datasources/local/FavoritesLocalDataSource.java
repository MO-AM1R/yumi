package com.example.yumi.data.favorite.datasources.local;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import java.util.List;
import java.util.Set;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public interface FavoritesLocalDataSource {
    Completable addFavorite(MealWithIngredients meal);
    Completable removeFavorite(String mealId);
    Flowable<List<MealWithIngredients>> getAllFavorites();
    Flowable<Boolean> isFavorite(String mealId);
    Flowable<Set<String>> getAllFavoriteIds();
    Completable clearAllFavorites();
}
