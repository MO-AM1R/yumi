package com.example.yumi.data.meals.model.responses;
import com.example.yumi.data.meals.model.dto.IngredientDto;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;


public class IngredientsResponse {
    @SerializedName("meals")
    private final List<IngredientDto> ingredients;

    public IngredientsResponse() {
        ingredients = new ArrayList<>();
    }

    public IngredientsResponse(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }
}