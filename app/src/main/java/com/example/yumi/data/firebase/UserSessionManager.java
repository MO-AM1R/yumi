package com.example.yumi.data.firebase;
import com.example.yumi.domain.user.model.User;


public class UserSessionManager {
    private User currentUser;

    public UserSessionManager() {}


    public synchronized void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    public synchronized void clearSession() {
        currentUser = null;
    }
}