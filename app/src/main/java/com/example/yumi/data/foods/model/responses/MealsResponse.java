package com.example.yumi.data.foods.model.responses;
import com.example.yumi.data.foods.model.dto.MealDto;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class MealsResponse {
    @SerializedName("meals")
    private List<MealDto> meals;

    public List<MealDto> getMeals() {
        return meals;
    }
}