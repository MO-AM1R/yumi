package com.example.yumi.presentation.authentication.view.activities;
import static com.example.yumi.data.config.SharedPreferencesKeysConfig.KEY_LOGGED_IN;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import com.example.yumi.R;
import com.example.yumi.app.BaseActivity;
import com.example.yumi.data.config.SharedPreferencesKeysConfig;
import com.example.yumi.presentation.home.view.activities.HomeBaseActivity;


public class AuthenticationActivity extends BaseActivity {
    private NavController navController;
    private SharedPreferences prefs;
    private boolean splashShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authentication);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_activity), (v, insets) -> {
            v.setPadding(0, 0, 0, 0);
            return insets;
        });

        Intent intent = getIntent();
        boolean splash = intent.getBooleanExtra("SPLASH", true);

        if (!splash){
            splashShown = true;
        }
        else if (savedInstanceState == null){
            splashShown = false;
        }else{
            splashShown = savedInstanceState.getBoolean("splashShown");
        }

        prefs = getSharedPreferences(SharedPreferencesKeysConfig.PREF_NAME, MODE_PRIVATE);
        setupNavigation();
    }

    private void setupNavigation() {
        FragmentContainerView fragmentContainerView = findViewById(R.id.nav_host_fragment);
        NavHostFragment navHostFragment = fragmentContainerView.getFragment();

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            NavGraph navGraph = navController.getNavInflater()
                    .inflate(R.navigation.authentication_nav_graph);

                int startDestination = determineStartDestination();
                if (startDestination == -1)
                    return;

                Log.d("Auth", "Start Destination: " + startDestination);
                navGraph.setStartDestination(startDestination);

            navController.setGraph(navGraph);
        } else {
            Log.e("Bug", "NavHostFragment is still null!");
        }
    }

    private int determineStartDestination() {
        boolean onboardingCompleted = prefs.getBoolean(SharedPreferencesKeysConfig.KEY_ONBOARDING_COMPLETED, false);

        if (!splashShown) {
            return R.id.splashFragment;
        } else if (!onboardingCompleted) {
            return R.id.onboardingFragment;
        } else {
            boolean loggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
            if (loggedIn){
                navigateToHome();
                return -1;
            }else{
                return R.id.loginFragment;
            }
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeBaseActivity.class);
        startActivity(intent);

        this.finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("splashShown", splashShown);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void markSplashSeen() {
        splashShown = true;
    }
}