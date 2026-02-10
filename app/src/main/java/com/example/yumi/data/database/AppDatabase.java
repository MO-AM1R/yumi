package com.example.yumi.data.database;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.yumi.data.database.doa.FavoriteDao;
import com.example.yumi.data.database.doa.IngredientDao;
import com.example.yumi.data.database.doa.MealDao;
import com.example.yumi.data.database.doa.MealPlanDao;
import com.example.yumi.data.database.entity.FavoriteEntity;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.entity.MealIngredientEntity;
import com.example.yumi.data.database.entity.MealPlanEntity;


@Database(
        entities = {
                MealEntity.class, MealIngredientEntity.class,
                FavoriteEntity.class, MealPlanEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract MealDao mealDao();
    public abstract IngredientDao ingredientDao();
    public abstract FavoriteDao favoriteDao();
    public abstract MealPlanDao mealPlanDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "yumi"
                            )
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("AppDatabase", "Database created!");
                                }
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Log.d("AppDatabase", "Database opened!");
                                }
                            })
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
