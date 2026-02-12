package com.example.yumi.data.firebase.auth;
import android.content.Context;
import com.example.yumi.domain.user.model.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public interface FirebaseAuthService {

    Single<User> signInWithEmail(String email, String password);

    Single<User> signUpWithEmail(String email, String password, String displayName);

    Single<User> signInWithGoogle(Context context);

    Completable signOut();

    User getCurrentUser();

    boolean isUserLoggedIn();
}