package com.example.yumi.domain.user.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String uid;
    private final String email;
    private final String displayName;
    private UserSettings settings;
    private List<String> favoriteMealIds;

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.settings = new UserSettings();
        this.favoriteMealIds = new ArrayList<>();
    }

    public User(String uid, String email, String displayName, UserSettings settings, List<String> favoriteMealIds) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.settings = settings != null ? settings : new UserSettings();
        this.favoriteMealIds = favoriteMealIds != null ? favoriteMealIds : new ArrayList<>();
    }

    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public UserSettings getSettings() { return settings; }
    public List<String> getFavoriteMealIds() { return favoriteMealIds; }

    public void setSettings(UserSettings settings) { this.settings = settings; }
    public void setFavoriteMealIds(List<String> favoriteMealIds) { this.favoriteMealIds = favoriteMealIds; }

    public static class Builder {
        private String uid;
        private String email;
        private String displayName;
        private UserSettings settings;
        private List<String> favoriteMealIds;

        public Builder uid(String uid) { this.uid = uid; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Builder settings(UserSettings settings) { this.settings = settings; return this; }
        public Builder favoriteMealIds(List<String> favoriteMealIds) { this.favoriteMealIds = favoriteMealIds; return this; }

        public User build() {
            return new User(uid, email, displayName, settings, favoriteMealIds);
        }
    }
}