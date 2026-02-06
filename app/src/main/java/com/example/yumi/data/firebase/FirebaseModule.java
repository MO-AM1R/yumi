package com.example.yumi.data.firebase;
import android.content.Context;
import com.example.yumi.data.firebase.auth.FirebaseAuthService;
import com.example.yumi.data.firebase.auth.FirebaseAuthServiceImpl;
import com.example.yumi.data.firebase.auth.GoogleSignInHelper;
import com.example.yumi.data.firebase.firestore.FirestoreService;
import com.example.yumi.data.firebase.firestore.FirestoreServiceImpl;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class FirebaseModule {
    private static volatile FirebaseModule instance;
    private final FirebaseAuthService authService;
    private final FirestoreService firestoreService;
    private final GoogleSignInHelper googleSignInHelper;
    private final UserSessionManager userSessionManager;

    private FirebaseModule(Context context) {
        FirebaseApp.initializeApp(context);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        googleSignInHelper = new GoogleSignInHelper(context);
        userSessionManager = new UserSessionManager();
        firestoreService = new FirestoreServiceImpl(firestore);
        authService = new FirebaseAuthServiceImpl(firebaseAuth, googleSignInHelper);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            synchronized (FirebaseModule.class) {
                if (instance == null) {
                    instance = new FirebaseModule(context.getApplicationContext());
                }
            }
        }
    }

    public static FirebaseModule getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FirebaseModule not initialized. Call initialize() in Application class.");
        }
        return instance;
    }

    public FirebaseAuthService getAuthService() {
        return authService;
    }

    public FirestoreService getFirestoreService() {
        return firestoreService;
    }

    public GoogleSignInHelper getGoogleSignInHelper() {
        return googleSignInHelper;
    }

    public UserSessionManager getUserSessionManager() {
        return userSessionManager;
    }
}