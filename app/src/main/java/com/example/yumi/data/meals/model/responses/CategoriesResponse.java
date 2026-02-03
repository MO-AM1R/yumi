package com.example.yumi.data.meals.model.responses;
import com.example.yumi.data.meals.model.dto.CategoryDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {

    @SerializedName("categories")
    private List<CategoryDto> categories;

    public List<CategoryDto> getCategories() {
        return categories;
    }
}