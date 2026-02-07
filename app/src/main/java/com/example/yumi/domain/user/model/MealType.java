package com.example.yumi.domain.user.model;

public enum MealType {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner"),
    SNACK("snack");

    private final String value;

    MealType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MealType fromValue(String value) {
        for (MealType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown meal type: " + value);
    }

    public static String fromMealType(MealType type){
        switch (type){
            case SNACK: return "Snack";
            case BREAKFAST: return "Breakfast";
            case DINNER: return "Dinner";
            default: return "Lunch";
        }
    }
}
