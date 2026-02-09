package com.example.yumi.data.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.yumi.data.database.doa.IngredientDao;
import com.example.yumi.data.database.doa.MealDao;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.entity.MealIngredientEntity;


@Database(entities = {MealEntity.class, MealIngredientEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract MealDao getMealDoa();
    public abstract IngredientDao getIngredientDoa();


    synchronized public static void init(Context context){
        if (instance == null){
            instance = Room
                    .databaseBuilder(context, AppDatabase.class, "yumi")
                    .build();
        }
    }

    public static AppDatabase getInstance(){
        return instance;
    }
}
