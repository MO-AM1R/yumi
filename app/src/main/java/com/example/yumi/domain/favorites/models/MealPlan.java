package com.example.yumi.domain.favorites.models;

import com.example.yumi.domain.user.model.MealType;

import java.util.HashMap;
import java.util.Map;

public class MealPlan {
    private Map<String, DayMeals> days;

    public MealPlan() {
        this.days = new HashMap<>();
    }

    public MealPlan(Map<String, DayMeals> days) {
        this.days = days != null ? days : new HashMap<>();
    }

    public Map<String, DayMeals> getDays() { return days; }
    public void setDays(Map<String, DayMeals> days) { this.days = days; }

    public DayMeals getDayMeals(String date) {
        return days.get(date);
    }

    public void setDayMeals(String date, DayMeals dayMeals) {
        if (dayMeals != null && !dayMeals.isEmpty()) {
            days.put(date, dayMeals);
        } else {
            days.remove(date);
        }
    }

    public void removeDayMeals(String date) {
        days.remove(date);
    }

    public boolean hasMealsForDay(String date) {
        DayMeals dayMeals = days.get(date);
        return dayMeals != null && !dayMeals.isEmpty();
    }

    public void setMealForDay(String date, MealType mealType, String mealId) {
        DayMeals dayMeals = days.getOrDefault(date, new DayMeals());
        if (dayMeals == null){
            dayMeals = new DayMeals();
        }

        dayMeals.setMeal(mealType, mealId);
        days.put(date, dayMeals);
    }

    public void removeMealFromDay(String date, MealType mealType) {
        DayMeals dayMeals = days.getOrDefault(date, new DayMeals());
        if (dayMeals == null){
            dayMeals = new DayMeals();
        }

        dayMeals.clearMeal(mealType);
        days.put(date, dayMeals);
    }
}