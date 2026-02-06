package com.example.yumi.domain.meals.model;

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
}
