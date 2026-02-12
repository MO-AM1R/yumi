package com.example.yumi.data.firebase.auth;
import android.content.Context;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;
import com.example.yumi.R;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import java.util.concurrent.Executors;


public class GoogleSignInHelper {

    private final CredentialManager credentialManager;
    private final String serverClientId;

    public GoogleSignInHelper(Context context) {
        this.credentialManager = CredentialManager.create(context);
        this.serverClientId = context.getString(R.string.default_web_client_id);
    }

    public void signIn(Context context, GoogleSignInCallback callback) {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(true)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CancellationSignal cancellationSignal = new CancellationSignal();

        credentialManager.getCredentialAsync(
                context,
                request,
                cancellationSignal,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignInResult(result, callback);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        handleSignInError(e, callback);
                    }
                }
        );
    }

    private void handleSignInResult(GetCredentialResponse response, GoogleSignInCallback callback) {
        Credential credential = response.getCredential();

        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;

            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(customCredential.getType())) {
                try {
                    GoogleIdTokenCredential googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(customCredential.getData());

                    String idToken = googleIdTokenCredential.getIdToken();

                    GoogleSignInResult result = new GoogleSignInResult(
                            idToken,
                            googleIdTokenCredential.getUniqueId(),
                            googleIdTokenCredential.getDisplayName()
                    );

                    callback.onSuccess(result);

                } catch (Exception e) {
                    callback.onError(new GoogleSignInException("Failed to process Google Sign-In", e));
                }
            } else {
                callback.onError(new GoogleSignInException("Unexpected credential type: " + customCredential.getType()));
            }
        } else {
            callback.onError(new GoogleSignInException("Invalid credential type"));
        }
    }

    private void handleSignInError(GetCredentialException e, GoogleSignInCallback callback) {
        String errorMessage;

        if (e instanceof NoCredentialException) {
            errorMessage = "No Google accounts found on this device";
        } else if (e.getMessage() != null && e.getMessage().contains("User cancelled")) {
            errorMessage = "Sign-in cancelled";
        } else {
            errorMessage = "Google Sign-In failed: " + (e.getMessage() != null ? e.getMessage() : "Unknown error");
        }

        callback.onError(new GoogleSignInException(errorMessage, e));
    }

    public interface GoogleSignInCallback {
        void onSuccess(GoogleSignInResult result);
        void onError(GoogleSignInException exception);
    }

    public static class GoogleSignInResult {
        private final String idToken;
        private final String email;
        private final String displayName;

        public GoogleSignInResult(String idToken, String email, String displayName) {
            this.idToken = idToken;
            this.email = email;
            this.displayName = displayName;
        }

        public String getIdToken() { return idToken; }
        public String getEmail() { return email; }
        public String getDisplayName() { return displayName; }
    }

    public static class GoogleSignInException extends Exception {
        public GoogleSignInException(String message) {
            super(message);
        }

        public GoogleSignInException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}