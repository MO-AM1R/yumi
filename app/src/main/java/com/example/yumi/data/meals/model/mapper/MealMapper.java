package com.example.yumi.data.meals.model.mapper;
import com.example.yumi.data.config.APIConfig;
import com.example.yumi.data.meals.model.dto.MealDto;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealIngredient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class MealMapper {
    public static Meal mapToDomain(MealDto dto) {
        if (dto == null) {
            return null;
        }

        return new Meal(
                orEmpty(dto.getId()),
                orEmpty(dto.getName()),
                dto.getAlternateName(),
                orEmpty(dto.getCategory()),
                orEmpty(dto.getArea()),
                orEmpty(dto.getInstructions()),
                orEmpty(dto.getThumbnailUrl()),
                parseTags(dto.getTags()),
                dto.getYoutubeUrl(),
                dto.getSourceUrl(),
                extractIngredients(dto)
        );
    }

    public static List<Meal> mapToDomainList(List<MealDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Meal> meals = new ArrayList<>();
        for (MealDto dto : dtoList) {
            Meal meal = mapToDomain(dto);
            if (meal != null) {
                meals.add(meal);
            }
        }
        return meals;
    }

    private static List<String> parseTags(String tags) {
        if (tags == null || tags.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> tagList = new ArrayList<>();
        String[] tagArray = tags.split(",");
        for (String tag : tagArray) {
            String trimmedTag = tag.trim();
            if (!trimmedTag.isEmpty()) {
                tagList.add(trimmedTag);
            }
        }
        return tagList;
    }

    private static List<MealIngredient> extractIngredients(MealDto dto) {
        List<String> ingredientsList = Arrays.asList(
                dto.getIngredient1(), dto.getIngredient2(), dto.getIngredient3(),
                dto.getIngredient4(), dto.getIngredient5(), dto.getIngredient6(),
                dto.getIngredient7(), dto.getIngredient8(), dto.getIngredient9(),
                dto.getIngredient10(), dto.getIngredient11(), dto.getIngredient12(),
                dto.getIngredient13(), dto.getIngredient14(), dto.getIngredient15(),
                dto.getIngredient16(), dto.getIngredient17(), dto.getIngredient18(),
                dto.getIngredient19(), dto.getIngredient20()
        );

        List<String> measuresList = Arrays.asList(
                dto.getMeasure1(), dto.getMeasure2(), dto.getMeasure3(),
                dto.getMeasure4(), dto.getMeasure5(), dto.getMeasure6(),
                dto.getMeasure7(), dto.getMeasure8(), dto.getMeasure9(),
                dto.getMeasure10(), dto.getMeasure11(), dto.getMeasure12(),
                dto.getMeasure13(), dto.getMeasure14(), dto.getMeasure15(),
                dto.getMeasure16(), dto.getMeasure17(), dto.getMeasure18(),
                dto.getMeasure19(), dto.getMeasure20()
        );

        List<MealIngredient> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientsList.size(); i++) {
            String ingredient = ingredientsList.get(i);
            String measure = measuresList.get(i);

            if (isNotBlank(ingredient)) {
                String trimmedIngredient = ingredient.trim();
                String trimmedMeasure = measure != null ? measure.trim() : "";
                String thumbnailUrl = APIConfig.INGREDIENTS_IMAGE_BASE_URL + trimmedIngredient + ".png";

                ingredients.add(new MealIngredient(
                        trimmedIngredient,
                        trimmedMeasure,
                        thumbnailUrl
                ));
            }
        }

        return ingredients;
    }

    private static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private static String orEmpty(String str) {
        return str != null ? str : "";
    }
}