package com.example.yumi.data.database.doa;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.yumi.data.database.entity.FavoriteEntity;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE mealId = :mealId")
    Completable deleteFavorite(String mealId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE mealId = :mealId)")
    Flowable<Boolean> isFavorite(String mealId);

    @Transaction
    @Query("SELECT m.* FROM meals m INNER JOIN favorites f ON m.mealId = f.mealId ORDER BY f.addedAt DESC")
    Flowable<List<MealWithIngredients>> getAllFavorites();

    @Query("SELECT mealId FROM favorites")
    Flowable<List<String>> getAllFavoriteIds();

    @Query("DELETE FROM favorites")
    Completable clearAllFavorites();
}
