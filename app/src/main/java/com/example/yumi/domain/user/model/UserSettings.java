package com.example.yumi.domain.user.model;

public class UserSettings {
    private boolean darkMode;
    private String language;

    public UserSettings() {
        this.darkMode = false;
        this.language = "en";
    }

    public UserSettings(boolean darkMode, String language) {
        this.darkMode = darkMode;
        this.language = language != null ? language : "en";
    }

    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}