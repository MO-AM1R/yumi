package com.example.yumi.data.database.mapper;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.entity.MealIngredientEntity;
import com.example.yumi.data.database.pojo.MealWithIngredients;
import com.example.yumi.data.database.pojo.PlanEntryWithMeal;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealIngredient;
import com.example.yumi.domain.user.model.MealType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MealPlannerMapper {

    private MealPlannerMapper() { }

    public static Map<MealType, Meal> toPlanDayMap(List<PlanEntryWithMeal> entries) {
        Map<MealType, Meal> dayMeals = new HashMap<>();

        if (entries == null || entries.isEmpty()) {
            return dayMeals;
        }

        for (PlanEntryWithMeal entry : entries) {
            if (entry == null || entry.getPlanEntry() == null) {
                continue;
            }

            try {
                MealType mealType = MealType.valueOf(entry.getPlanEntry().getMealType());
                Meal meal = toMeal(entry.getMealWithIngredients());

                if (meal != null) {
                    dayMeals.put(mealType, meal);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        return dayMeals;
    }

    public static Map<String, Map<MealType, Meal>> toFullPlanMap(List<PlanEntryWithMeal> entries) {
        Map<String, Map<MealType, Meal>> fullPlan = new HashMap<>();

        if (entries == null || entries.isEmpty()) {
            return fullPlan;
        }

        for (PlanEntryWithMeal entry : entries) {
            if (entry == null || entry.getPlanEntry() == null) {
                continue;
            }

            String date = entry.getPlanEntry().getDate();

            try {
                MealType mealType = MealType.valueOf(entry.getPlanEntry().getMealType());
                Meal meal = toMeal(entry.getMealWithIngredients());

                if (meal != null) {
                    if (!fullPlan.containsKey(date)) {
                        fullPlan.put(date, new HashMap<>());
                    }

                    Objects.requireNonNull(fullPlan.get(date)).put(mealType, meal);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        return fullPlan;
    }

    public static Meal toMeal(MealWithIngredients mealWithIngredients) {
        if (mealWithIngredients == null || mealWithIngredients.getMeal() == null) {
            return null;
        }

        MealEntity entity = mealWithIngredients.getMeal();
        List<MealIngredient> ingredients = toIngredientList(mealWithIngredients.getIngredients());

        return new Meal(
                entity.getMealId(),
                entity.getName(),
                "",
                entity.getCategory(),
                entity.getArea(),
                entity.getInstructions(),
                entity.getThumbnailUrl(),
                new ArrayList<>(),
                entity.getYoutubeUrl(),
                "",
                ingredients
        );
    }

    public static List<MealIngredient> toIngredientList(List<MealIngredientEntity> entities) {
        List<MealIngredient> ingredients = new ArrayList<>();

        if (entities == null || entities.isEmpty()) {
            return ingredients;
        }

        for (MealIngredientEntity entity : entities) {
            if (entity != null) {
                ingredients.add(new MealIngredient(
                        entity.getName(),
                        entity.getMeasure(),
                        entity.getThumbnailUrl()
                ));
            }
        }

        return ingredients;
    }
}