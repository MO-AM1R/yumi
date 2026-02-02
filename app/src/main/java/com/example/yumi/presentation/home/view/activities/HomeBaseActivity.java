package com.example.yumi.presentation.home.view.activities;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import com.example.yumi.R;
import com.example.yumi.databinding.ActivityHomeBaseBinding;
import com.example.yumi.presentation.home.view.fragment.FavoritesFragment;
import com.example.yumi.presentation.home.view.fragment.HomeFragment;
import com.example.yumi.presentation.home.view.fragment.ProfileFragment;
import com.example.yumi.presentation.home.view.fragment.SearchFragment;
import nl.joery.animatedbottombar.AnimatedBottomBar;


public class HomeBaseActivity extends AppCompatActivity {
    private ActivityHomeBaseBinding binding;
    private AnimatedBottomBar bottomBar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomBar = binding.bottomBar;
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int oldIndex, @Nullable AnimatedBottomBar.Tab oldTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = getFragmentByTabId(newTab.getId());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }

            @Override
            public void onTabReselected(int index, @NonNull AnimatedBottomBar.Tab tab) {
                Log.d("BottomNavBar", "onTabReselected " + index);
            }
        });

        setupBackPressHandler();
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
    }

    Fragment getFragmentByTabId(int tabId){
        if (R.id.calendarFragment == tabId){
            return new HomeFragment();
        }else if (R.id.searchFragment == tabId){
             return new SearchFragment();
        } else if (R.id.favoritesFragment == tabId) {
            return new FavoritesFragment();
        }else if (R.id.profileFragment == tabId){
            return new ProfileFragment();
        }

        return new HomeFragment();
    }
}