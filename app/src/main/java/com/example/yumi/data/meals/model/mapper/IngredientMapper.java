package com.example.yumi.data.meals.model.mapper;

import com.example.yumi.data.config.APIConfig;
import com.example.yumi.data.meals.model.dto.IngredientDto;
import com.example.yumi.domain.meals.model.Ingredient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IngredientMapper {
    public static Ingredient mapToDomain(IngredientDto dto) {
        if (dto == null) {
            return null;
        }

        String ingredientName = dto.getName() != null ? dto.getName() : "";
        String thumbnailUrl = dto.getThumbnailUrl() != null
                ? dto.getThumbnailUrl()
                : APIConfig.INGREDIENTS_IMAGE_BASE_URL + ingredientName + ".png";

        return new Ingredient(
                orEmpty(dto.getId()),
                ingredientName,
                dto.getDescription(),
                thumbnailUrl,
                dto.getType()
        );
    }

    public static List<Ingredient> mapToDomainList(List<IngredientDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDto dto : dtoList) {
            Ingredient ingredient = mapToDomain(dto);
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    public static String getIngredientImageUrl(String ingredientName) {
        if (ingredientName == null || ingredientName.isEmpty()) {
            return "";
        }
        return APIConfig.INGREDIENTS_IMAGE_BASE_URL + ingredientName + ".png";
    }

    private static String orEmpty(String str) {
        return str != null ? str : "";
    }
}