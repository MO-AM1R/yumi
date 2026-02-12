package com.example.yumi.data.user.datasources.remote;
import android.content.Context;
import com.example.yumi.domain.user.model.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public interface UserRemoteDataSource {
    Single<User> signInWithEmail(String email, String password);
    Single<User> signUpWithEmail(String email, String password, String displayName);
    Single<User> signInWithGoogle(Context context);
    Completable signOut();
    User getCurrentAuthUser();
    boolean isUserLoggedIn();
    Completable createUser(User user);
    Single<User> getUser(String userId);
    Completable syncUserData(User user);
}
