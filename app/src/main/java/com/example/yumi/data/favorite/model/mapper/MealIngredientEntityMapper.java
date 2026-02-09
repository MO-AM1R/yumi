package com.example.yumi.data.favorite.model.mapper;
import com.example.yumi.data.database.entity.MealIngredientEntity;
import com.example.yumi.domain.meals.model.MealIngredient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class MealIngredientEntityMapper {
    public static MealIngredientEntity toEntity(
            MealIngredient ingredient,
            String mealOwnerId
    ) {
        if (ingredient == null || mealOwnerId == null) {
            return null;
        }

        return new MealIngredientEntity(
                ingredient.getName(),
                mealOwnerId,
                ingredient.getMeasure(),
                ingredient.getThumbnailUrl()
        );
    }

    public static List<MealIngredientEntity> toEntityList(
            List<MealIngredient> ingredients,
            String mealOwnerId
    ) {
        if (ingredients == null || ingredients.isEmpty()) {
            return Collections.emptyList();
        }

        List<MealIngredientEntity> entities = new ArrayList<>();
        for (MealIngredient ingredient : ingredients) {
            MealIngredientEntity entity = toEntity(ingredient, mealOwnerId);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static MealIngredient toDomain(MealIngredientEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MealIngredient(
                entity.getName(),
                entity.getMeasure(),
                entity.getThumbnailUrl()
        );
    }

    public static List<MealIngredient> toDomainList(
            List<MealIngredientEntity> entities
    ) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<MealIngredient> ingredients = new ArrayList<>();
        for (MealIngredientEntity entity : entities) {
            MealIngredient ingredient = toDomain(entity);
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
}
