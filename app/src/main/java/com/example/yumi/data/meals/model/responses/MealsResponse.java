package com.example.yumi.data.meals.model.responses;
import com.example.yumi.data.meals.model.dto.MealDto;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;


public class MealsResponse {
    @SerializedName("meals")
    private final List<MealDto> meals;

    public MealsResponse() {
        meals = new ArrayList<>();
    }

    public MealsResponse(List<MealDto> meals) {
        this.meals = meals;
    }

    public List<MealDto> getMeals() {
        return meals;
    }
}