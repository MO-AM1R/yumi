package com.example.yumi.data.database.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.yumi.data.database.entity.MealEntity;
import com.example.yumi.data.database.entity.MealIngredientEntity;
import java.util.List;


public class MealWithIngredients {
    @Embedded
    private MealEntity meal;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "mealOwnerId"
    )
    private List<MealIngredientEntity> ingredients;

    public MealWithIngredients() {}

    public MealWithIngredients(MealEntity meal, List<MealIngredientEntity> ingredients) {
        this.meal = meal;
        this.ingredients = ingredients;
    }

    public MealEntity getMeal() {
        return meal;
    }

    public void setMeal(MealEntity meal) {
        this.meal = meal;
    }

    public List<MealIngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<MealIngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }
}
