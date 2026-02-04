package com.example.yumi.data.meals.model.responses;
import com.example.yumi.data.meals.model.dto.AreaDto;
import com.example.yumi.data.meals.model.dto.CategoryDto;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoriesResponse {

    @SerializedName("categories")
    private final List<CategoryDto> categories;

    public CategoriesResponse() {
        categories = new ArrayList<>();
    }

    public CategoriesResponse(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }
}