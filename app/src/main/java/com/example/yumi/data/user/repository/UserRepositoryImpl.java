package com.example.yumi.data.user.repository;
import com.example.yumi.data.firebase.FirebaseModule;
import com.example.yumi.data.user.datasources.local.UserLocalDataSource;
import com.example.yumi.data.user.datasources.local.UserLocalDataSourceImpl;
import com.example.yumi.data.user.datasources.remote.UserRemoteDataSource;
import com.example.yumi.data.user.datasources.remote.UserRemoteDataSourceImpl;
import com.example.yumi.domain.user.repository.UserRepository;
import android.content.Context;
import com.example.yumi.domain.user.model.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class UserRepositoryImpl implements UserRepository {
    private final UserRemoteDataSource remoteDataSource;
    private final UserLocalDataSource userLocalDataSource;

    public UserRepositoryImpl(Context context) {
        FirebaseModule module = FirebaseModule.getInstance();
        this.remoteDataSource = new UserRemoteDataSourceImpl(
                module.getAuthService(),
                module.getFirestoreService()
        );
        this.userLocalDataSource = new UserLocalDataSourceImpl(
                context
        );
    }


    @Override
    public Single<User> signInWithEmail(String email, String password) {
        return remoteDataSource.signInWithEmail(email, password)
                .flatMap(this::handleAuthSuccess);
    }

    @Override
    public Single<User> signUpWithEmail(String email, String password, String displayName) {
        return remoteDataSource.signUpWithEmail(email, password, displayName)
                .flatMap(user -> remoteDataSource.createUser(user)
                        .andThen(Single.just(user))
                        .onErrorResumeNext(e -> Single.just(user)))
                .flatMap(this::handleAuthSuccess);
    }

    @Override
    public Single<User> signInWithGoogle(Context context) {
        return remoteDataSource.signInWithGoogle(context)
                .flatMap(user -> remoteDataSource.createUser(user)
                        .andThen(Single.just(user))
                        .onErrorResumeNext(e -> Single.just(user)))
                .flatMap(this::handleAuthSuccess);
    }

    @Override
    public Completable signOut() {
        return remoteDataSource.signOut()
                .doOnComplete(() -> {
                    userLocalDataSource.clearSession();
                    userLocalDataSource.clearUserData();
                });
    }

    @Override
    public User getCurrentUser() {
        User cachedUser = userLocalDataSource.getCurrentUser();
        if (cachedUser != null) {
            return cachedUser;
        }
        return remoteDataSource.getCurrentAuthUser();
    }

    @Override
    public boolean isLoggedIn() {
        return userLocalDataSource.isLoggedIn() && remoteDataSource.isUserLoggedIn();
    }
    @Override
    public void setOnboardingCompleted(boolean completed) {
        userLocalDataSource.setOnboardingCompleted(completed);
    }

    @Override
    public boolean isOnboardingCompleted() {
        return userLocalDataSource.isOnboardingCompleted();
    }

    @Override
    public Completable syncData(User user) {
        return remoteDataSource.syncUserData(user);
    }

    @Override
    public Single<User> retrieveData() {
        String currentUserId = userLocalDataSource.getUserId();
        return remoteDataSource.getUser(currentUserId);
    }

    private Single<User> handleAuthSuccess(User user) {
        userLocalDataSource.setLoggedIn(true);
        userLocalDataSource.setUserId(user.getUid());

        return remoteDataSource.getUser(user.getUid())
                .doOnSuccess(userLocalDataSource::setCurrentUser)
                .onErrorResumeNext(e -> {
                    userLocalDataSource.setCurrentUser(user);
                    return Single.just(user);
                });
    }
}
