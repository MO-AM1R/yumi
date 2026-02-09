package com.example.yumi.data.favorite.model.mapper;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.domain.meals.model.Meal;


public abstract class MealEntityMapper {
    public static MealEntity toEntity(Meal meal) {
        if (meal == null) {
            return null;
        }

        return new MealEntity(
                meal.getId(),
                meal.getName(),
                meal.getCategory(),
                meal.getArea(),
                meal.getInstructions(),
                meal.getThumbnailUrl(),
                meal.getYoutubeUrl()
        );
    }

    public static Meal toDomain(MealEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Meal(
                orEmpty(entity.getMealId()),
                orEmpty(entity.getName()),
                "",
                orEmpty(entity.getCategory()),
                orEmpty(entity.getArea()),
                orEmpty(entity.getInstructions()),
                orEmpty(entity.getThumbnailUrl()),
                new java.util.ArrayList<>(),
                orEmpty(entity.getYoutubeUrl()),
                "",
                new java.util.ArrayList<>()
        );
    }

    private static String orEmpty(String value) {
        return value != null ? value : "";
    }
}
