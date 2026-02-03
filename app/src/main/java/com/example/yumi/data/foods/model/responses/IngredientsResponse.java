package com.example.yumi.data.foods.model.responses;
import com.example.yumi.data.foods.model.dto.IngredientDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class IngredientsResponse {
    @SerializedName("meals")
    private List<IngredientDto> ingredients;

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }
}