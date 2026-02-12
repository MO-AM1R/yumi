package com.example.yumi.data.firebase;
import com.example.yumi.domain.user.model.User;


public final class UserSessionManager {

    private static volatile UserSessionManager instance;
    private User currentUser;

    private UserSessionManager() { }

    public static synchronized UserSessionManager getInstance() {
        if (instance == null) {
            synchronized (UserSessionManager.class) {
                if (instance == null) {
                    instance = new UserSessionManager();
                }
            }
        }
        return instance;
    }

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
