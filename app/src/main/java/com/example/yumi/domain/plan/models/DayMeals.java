package com.example.yumi.domain.plan.models;
import com.example.yumi.domain.user.model.MealType;

public class DayMeals {
    private String breakfast;
    private String lunch;
    private String dinner;
    private String snack;

    public DayMeals() {
        this.breakfast = null;
        this.lunch = null;
        this.dinner = null;
        this.snack = null;
    }

    public DayMeals(String breakfast, String lunch, String dinner, String snack) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
    }

    public DayMeals(MealType mealType, String mealId) {
        switch (mealType){
            case LUNCH: {
                lunch = mealId;
                break;
            }
            case SNACK: {
                snack = mealId;
                break;
            }
            case DINNER:{
                dinner = mealId;
                break;
            }
            default:{
                breakfast = mealId;
                break;
            }
        }
    }

    public String getBreakfast() { return breakfast; }
    public void setBreakfast(String breakfast) { this.breakfast = breakfast; }

    public String getLunch() { return lunch; }
    public void setLunch(String lunch) { this.lunch = lunch; }

    public String getDinner() { return dinner; }
    public void setDinner(String dinner) { this.dinner = dinner; }

    public String getSnack() { return snack; }
    public void setSnack(String snack) { this.snack = snack; }

    public void setMeal(MealType type, String mealId) {
        switch (type) {
            case BREAKFAST:
                this.breakfast = mealId;
                break;
            case LUNCH:
                this.lunch = mealId;
                break;
            case DINNER:
                this.dinner = mealId;
                break;
            case SNACK:
                this.snack = mealId;
                break;
        }
    }

    public String getMeal(MealType type) {
        switch (type) {
            case BREAKFAST:
                return breakfast;
            case LUNCH:
                return lunch;
            case DINNER:
                return dinner;
            case SNACK:
                return snack;
            default:
                return null;
        }
    }

    public void clearMeal(MealType type) {
        setMeal(type, null);
    }

    public boolean isEmpty() {
        return breakfast == null && lunch == null && dinner == null && snack == null;
    }

    public int getMealsCount() {
        int count = 0;
        if (breakfast != null) count++;
        if (lunch != null) count++;
        if (dinner != null) count++;
        if (snack != null) count++;
        return count;
    }

    public boolean hasMeal(MealType type) {
        return getMeal(type) != null;
    }
}