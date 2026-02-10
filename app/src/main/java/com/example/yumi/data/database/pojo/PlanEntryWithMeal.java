package com.example.yumi.data.database.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.entity.MealPlanEntity;

public class PlanEntryWithMeal {
    @Embedded
    private MealPlanEntity planEntry;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "mealId",
            entity = MealEntity.class
    )
    private MealWithIngredients mealWithIngredients;

    public PlanEntryWithMeal() {}

    public MealPlanEntity getPlanEntry() {
        return planEntry;
    }

    public void setPlanEntry(MealPlanEntity planEntry) {
        this.planEntry = planEntry;
    }

    public MealWithIngredients getMealWithIngredients() {
        return mealWithIngredients;
    }

    public void setMealWithIngredients(MealWithIngredients mealWithIngredients) {
        this.mealWithIngredients = mealWithIngredients;
    }
}
