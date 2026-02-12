package com.example.yumi.data.firebase.auth;
import android.content.Context;
import com.example.yumi.domain.user.model.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class FirebaseAuthServiceImpl implements FirebaseAuthService {
    private final FirebaseAuth firebaseAuth;
    private final GoogleSignInHelper googleSignInHelper;

    public FirebaseAuthServiceImpl(FirebaseAuth firebaseAuth, GoogleSignInHelper googleSignInHelper) {
        this.firebaseAuth = firebaseAuth;
        this.googleSignInHelper = googleSignInHelper;
    }

    @Override
    public Single<User> signInWithEmail(String email, String password) {
        return Single.create(emitter ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(result -> {
                            FirebaseUser firebaseUser = result.getUser();
                            if (firebaseUser != null) {
                                emitter.onSuccess(mapToUser(firebaseUser));
                            } else {
                                emitter.onError(new AuthException("Sign in failed"));
                            }
                        })
                        .addOnFailureListener(e -> emitter.onError(new AuthException(parseError(e))))
        );
    }

    @Override
    public Single<User> signUpWithEmail(String email, String password, String displayName) {
        return Single.create(emitter ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(result -> {
                            FirebaseUser firebaseUser = result.getUser();
                            if (firebaseUser != null) {
                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .build();

                                firebaseUser.updateProfile(profile)
                                        .addOnCompleteListener(task -> emitter.onSuccess(mapToUser(firebaseUser)));
                            } else {
                                emitter.onError(new AuthException("Sign up failed"));
                            }
                        })
                        .addOnFailureListener(e -> emitter.onError(new AuthException(parseError(e))))
        );
    }

    @Override
    public Single<User> signInWithGoogle(Context context) {
        return Single.create(emitter ->
                googleSignInHelper.signIn(context, new GoogleSignInHelper.GoogleSignInCallback() {
                    @Override
                    public void onSuccess(GoogleSignInHelper.GoogleSignInResult result) {
                        firebaseAuthWithGoogleInternal(result.getIdToken(), emitter);
                    }

                    @Override
                    public void onError(GoogleSignInHelper.GoogleSignInException exception) {
                        emitter.onError(new AuthException(exception.getMessage()));
                    }
                })
        );
    }

    @Override
    public Single<User> firebaseAuthWithGoogle(String idToken) {
        return Single.create(emitter -> firebaseAuthWithGoogleInternal(idToken, emitter));
    }

    private void firebaseAuthWithGoogleInternal(String idToken, io.reactivex.rxjava3.core.SingleEmitter<User> emitter) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(result -> {
                    FirebaseUser firebaseUser = result.getUser();
                    if (firebaseUser != null) {
                        emitter.onSuccess(mapToUser(firebaseUser));
                    } else {
                        emitter.onError(new AuthException("Google sign in failed"));
                    }
                })
                .addOnFailureListener(e -> emitter.onError(new AuthException(parseError(e))));
    }

    @Override
    public Completable signOut() {
        return Completable.fromAction(firebaseAuth::signOut);
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser != null ? mapToUser(firebaseUser) : null;
    }

    @Override
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    private User mapToUser(FirebaseUser firebaseUser) {
        return new User.Builder()
                .uid(firebaseUser.getUid())
                .email(firebaseUser.getEmail())
                .displayName(firebaseUser.getDisplayName())
                .build();
    }

    private String parseError(Exception e) {
        String message = e.getMessage();
        if (message == null) return "An error occurred";

        if (message.contains("INVALID_LOGIN_CREDENTIALS") || message.contains("WRONG_PASSWORD")) {
            return "Invalid email or password";
        } else if (message.contains("USER_NOT_FOUND")) {
            return "No account found with this email";
        } else if (message.contains("EMAIL_ALREADY_IN_USE")) {
            return "Email already in use";
        } else if (message.contains("WEAK_PASSWORD")) {
            return "Password is too weak";
        } else if (message.contains("INVALID_EMAIL")) {
            return "Invalid email address";
        } else if (message.contains("NETWORK")) {
            return "Network error. Check your connection";
        }

        return message;
    }

    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}