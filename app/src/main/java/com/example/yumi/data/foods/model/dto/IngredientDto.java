package com.example.yumi.data.foods.model.dto;

import com.example.yumi.data.config.APIConfig;
import com.example.yumi.domain.foods.model.Ingredient;
import com.google.gson.annotations.SerializedName;

public class IngredientDto {
    @SerializedName("idIngredient")
    private String id;

    @SerializedName("strIngredient")
    private String name;

    @SerializedName("strDescription")
    private String description;

    @SerializedName("strThumb")
    private String thumbnailUrl;

    @SerializedName("strType")
    private String type;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getType() {
        return type;
    }

    public Ingredient toIngredient() {
        String ingredientName = name != null ? name : "";
        String thumb = thumbnailUrl != null ? thumbnailUrl :
                APIConfig.INGREDIENTS_IMAGE_BASE_URL + ingredientName + ".png";

        return new Ingredient(
                orEmpty(id),
                ingredientName,
                description,
                thumb,
                type
        );
    }

    private String orEmpty(String str) {
        return str != null ? str : "";
    }
}