package com.example.yumi.data.favorite.model.mapper;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import com.example.yumi.domain.meals.model.Meal;


public abstract class MealWithIngredientsMapper {
    public static MealWithIngredients toPojo(Meal meal) {
        if (meal == null) {
            return null;
        }

        return new MealWithIngredients(
                MealEntityMapper.toEntity(meal),
                MealIngredientEntityMapper.toEntityList(
                        meal.getIngredients(),
                        meal.getId()
                )
        );
    }

    public static Meal toDomain(MealWithIngredients pojo) {
        if (pojo == null) {
            return null;
        }

        Meal meal = MealEntityMapper.toDomain(pojo.getMeal());
        meal.setIngredients(
                MealIngredientEntityMapper.toDomainList(
                        pojo.getIngredients()
                )
        );

        return meal;
    }
}
