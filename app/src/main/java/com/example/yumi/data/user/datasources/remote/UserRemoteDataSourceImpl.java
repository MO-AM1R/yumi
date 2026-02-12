package com.example.yumi.data.user.datasources.remote;
import android.content.Context;
import com.example.yumi.data.firebase.auth.FirebaseAuthService;
import com.example.yumi.data.firebase.firestore.FirestoreService;
import com.example.yumi.domain.user.model.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class UserRemoteDataSourceImpl implements UserRemoteDataSource {
    private final FirebaseAuthService authService;
    private final FirestoreService firestoreService;

    public UserRemoteDataSourceImpl(FirebaseAuthService authService, FirestoreService firestoreService) {
        this.authService = authService;
        this.firestoreService = firestoreService;
    }


    @Override
    public Single<User> signInWithEmail(String email, String password) {
        return authService.signInWithEmail(email, password);
    }

    @Override
    public Single<User> signUpWithEmail(String email, String password, String displayName) {
        return authService.signUpWithEmail(email, password, displayName);
    }

    @Override
    public Single<User> signInWithGoogle(Context context) {
        return authService.signInWithGoogle(context);
    }

    @Override
    public Completable signOut() {
        return authService.signOut();
    }

    @Override
    public User getCurrentAuthUser() {
        return authService.getCurrentUser();
    }

    @Override
    public boolean isUserLoggedIn() {
        return authService.isUserLoggedIn();
    }

    @Override
    public Completable createUser(User user) {
        return firestoreService.createUser(user);
    }

    @Override
    public Single<User> getUser(String userId) {
        return firestoreService.getUser(userId);
    }

    @Override
    public Completable syncUserData(User user) {
        return firestoreService.syncUserData(user);
    }
}