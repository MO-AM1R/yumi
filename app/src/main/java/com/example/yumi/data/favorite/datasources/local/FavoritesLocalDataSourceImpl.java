package com.example.yumi.data.favorite.datasources.local;
import android.content.Context;
import com.example.yumi.data.database.AppDatabase;
import com.example.yumi.data.database.doa.FavoriteDao;
import com.example.yumi.data.database.doa.IngredientDao;
import com.example.yumi.data.database.doa.MealDao;
import com.example.yumi.data.database.entity.FavoriteEntity;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public class FavoritesLocalDataSourceImpl implements FavoritesLocalDataSource {
    private final MealDao mealDao;
    private final FavoriteDao favoriteDao;
    private final IngredientDao ingredientDao;

    public FavoritesLocalDataSourceImpl(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);

        mealDao = db.mealDao();
        ingredientDao = db.ingredientDao();
        favoriteDao = db.favoriteDao();
    }

    @Override
    public Completable addFavorite(MealWithIngredients meal) {
        FavoriteEntity favoriteEntity = new FavoriteEntity(meal.getMeal().getMealId(),
                System.currentTimeMillis());

        return mealDao.insertMeal(meal.getMeal())
                .andThen(ingredientDao.insertIngredients(meal.getIngredients()))
                .andThen(favoriteDao.insertFavorite(favoriteEntity));
    }

    @Override
    public Completable removeFavorite(String mealId) {
        return favoriteDao.deleteFavorite(mealId)
                .andThen(deleteMealIfOrphan(mealId));
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
    public Flowable<List<MealWithIngredients>> getAllFavorites() {
        return favoriteDao.getAllFavorites();
    }

    @Override
    public Flowable<Boolean> isFavorite(String mealId) {
        return favoriteDao.isFavorite(mealId);
    }

    @Override
    public Flowable<Set<String>> getAllFavoriteIds() {
        return favoriteDao.getAllFavoriteIds().map(strings -> new HashSet<>() {{
            addAll(strings);
        }});
    }

    @Override
    public Completable clearAllFavorites() {
        return favoriteDao.getAllFavoriteIds()
                .flatMapCompletable(mealIds ->
                        favoriteDao.clearAllFavorites()
                );
    }
}