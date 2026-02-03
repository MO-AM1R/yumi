package com.example.yumi.data.foods.model.mapper;

import com.example.yumi.data.foods.model.dto.CategoryDto;
import com.example.yumi.domain.foods.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CategoryMapper {

    public static Category mapToDomain(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        return new Category(
                orEmpty(dto.getId()),
                orEmpty(dto.getName()),
                orEmpty(dto.getThumbnailUrl()),
                orEmpty(dto.getDescription())
        );
    }

    public static List<Category> mapToDomainList(List<CategoryDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Category> categories = new ArrayList<>();
        for (CategoryDto dto : dtoList) {
            Category category = mapToDomain(dto);
            if (category != null) {
                categories.add(category);
            }
        }
        return categories;
    }

    private static String orEmpty(String str) {
        return str != null ? str : "";
    }
}