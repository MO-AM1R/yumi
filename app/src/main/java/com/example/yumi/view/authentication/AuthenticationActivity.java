package com.example.yumi.view.authentication;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import com.example.yumi.R;
import com.example.yumi.common.utils.SharedPreferencesKeys;


public class AuthenticationActivity extends AppCompatActivity {
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

        if (savedInstanceState == null){
            splashShown = false;
        }else{
            splashShown = savedInstanceState.getBoolean("splashShown");
        }

        prefs = getSharedPreferences(SharedPreferencesKeys.PREF_NAME, MODE_PRIVATE);
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
                Log.d("Auth", "Start Destination: " + startDestination);
                navGraph.setStartDestination(startDestination);

            navController.setGraph(navGraph);
        } else {
            Log.e("Bug", "NavHostFragment is still null!");
        }
    }

    private int determineStartDestination() {
        boolean onboardingCompleted = prefs.getBoolean(SharedPreferencesKeys.KEY_ONBOARDING_COMPLETED, false);

        if (!splashShown) {
            return R.id.splashFragment;
        } else if (!onboardingCompleted) {
            return R.id.onboardingFragment;
        } else {
            return R.id.loginFragment;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("splashShown", true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void markSplashSeen() {
        splashShown = true;
    }
}