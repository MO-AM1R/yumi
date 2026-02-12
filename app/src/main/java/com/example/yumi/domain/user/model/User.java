package com.example.yumi.domain.user.model;
import androidx.annotation.NonNull;
import com.example.yumi.domain.plan.models.MealPlan;
import java.util.ArrayList;
import java.util.List;


public class User {
    private final String uid;
    private final String email;
    private final String displayName;
    private List<String> favoriteMealIds;
    private MealPlan mealPlan;

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.favoriteMealIds = new ArrayList<>();
    }

    public User(String uid, String email, String displayName, List<String> favoriteMealIds, MealPlan mealPlan) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.favoriteMealIds = favoriteMealIds != null ? favoriteMealIds : new ArrayList<>();
        this.mealPlan = mealPlan != null ? mealPlan : new MealPlan();
    }

    public void setPlannedMealsIds(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public List<String> getFavoriteMealIds() { return favoriteMealIds; }

    public void setFavoriteMealIds(List<String> favoriteMealIds) { this.favoriteMealIds = favoriteMealIds; }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", favoriteMealIds=" + favoriteMealIds +
                ", mealPlan=" + mealPlan +
                '}';
    }

    public static class Builder {
        private String uid;
        private String email;
        private String displayName;
        private List<String> favoriteMealIds;
        private MealPlan mealPlan;

        public Builder mealPlan(MealPlan mealPlan) { this.mealPlan = mealPlan; return this;}
        public Builder uid(String uid) { this.uid = uid; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Builder favoriteMealIds(List<String> favoriteMealIds) { this.favoriteMealIds = favoriteMealIds; return this; }
        public Builder fromUser(User user) {
            this.email = user.email;
            this.displayName = user.displayName;
            this.mealPlan = user.mealPlan;
            this.favoriteMealIds = user.favoriteMealIds;
            this.uid = user.uid;
            return this;
        }

        public User build() {
            return new User(uid, email, displayName, favoriteMealIds, mealPlan);
        }
    }
}