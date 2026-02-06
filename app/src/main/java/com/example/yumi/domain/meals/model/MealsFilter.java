package com.example.yumi.domain.meals.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MealsFilter {
    private MealsFilterType type;
    private String query;

    public MealsFilter() {
    }

    public MealsFilter(MealsFilterType type, String query) {
        this.type = type;
        this.query = query;
    }

    public MealsFilterType getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }

    public void setType(MealsFilterType type) {
        this.type = type;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @NonNull
    @Override
    public String toString() {
        if (Objects.requireNonNull(type) == MealsFilterType.AREA) {
            return "Meals with " + query;
        }
        return query + " Meals";
    }
}
