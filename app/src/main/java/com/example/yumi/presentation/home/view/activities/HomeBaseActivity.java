package com.example.yumi.presentation.home.view.activities;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.yumi.R;
import com.example.yumi.databinding.ActivityHomeBaseBinding;
import com.example.yumi.presentation.home.view.fragment.CalendarFragment;
import com.example.yumi.presentation.home.view.fragment.FavoritesFragment;
import com.example.yumi.presentation.home.view.fragment.HomeFragment;
import com.example.yumi.presentation.home.view.fragment.ProfileFragment;
import com.example.yumi.presentation.home.view.fragment.SearchFragment;
import nl.joery.animatedbottombar.AnimatedBottomBar;


public class HomeBaseActivity extends AppCompatActivity {

    private ActivityHomeBaseBinding binding;
    private Fragment activeFragment;
    private int currentTabIndex = 0;
    private static final String TAG_HOME = "home";
    private static final String TAG_SEARCH = "search";
    private static final String TAG_CALENDAR = "calendar";
    private static final String TAG_FAVORITES = "favorites";
    private static final String TAG_PROFILE = "profile";
    private static final String KEY_CURRENT_TAB = "current_tab";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            setupFragments();
        } else {
            currentTabIndex = savedInstanceState.getInt(KEY_CURRENT_TAB, 0);
            restoreActiveFragment();
        }

        setupBottomBar();
        setupBackPressHandler();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_TAB, currentTabIndex);
    }

    private void setupFragments() {
        Fragment homeFragment = new HomeFragment();
        activeFragment = homeFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment, TAG_HOME)
                .commit();
    }

    private void restoreActiveFragment() {
        String activeTag = getTagForIndex(currentTabIndex);
        activeFragment = getSupportFragmentManager().findFragmentByTag(activeTag);

        if (activeFragment == null) {
            activeFragment = getSupportFragmentManager().findFragmentByTag(TAG_HOME);
        }
        if (activeFragment == null) {
            setupFragments();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setupBottomBar() {
        binding.bottomBar.selectTabAt(currentTabIndex, true);

        binding.bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int oldIndex, @Nullable AnimatedBottomBar.Tab oldTab,
                                      int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                if (currentTabIndex != newIndex) {
                    currentTabIndex = newIndex;
                    switchFragment(newIndex);
                }
            }

            @Override
            public void onTabReselected(int index, @NonNull AnimatedBottomBar.Tab tab) {
                // Optional: scroll to top or refresh
            }
        });
    }

    private void switchFragment(int index) {
        String targetTag = getTagForIndex(index);
        Fragment targetFragment = getSupportFragmentManager().findFragmentByTag(targetTag);

        if (targetFragment != null && targetFragment == activeFragment) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        if (activeFragment != null) {
            transaction.hide(activeFragment);
        }

        if (targetFragment == null) {
            targetFragment = createFragment(index);
            transaction.add(R.id.fragment_container, targetFragment, targetTag);
        } else {
            transaction.show(targetFragment);
        }

        transaction.commit();
        activeFragment = targetFragment;
    }

    private String getTagForIndex(int index) {
        switch (index) {
            case 0:
                return TAG_HOME;
            case 1:
                return TAG_SEARCH;
            case 2:
                return TAG_CALENDAR;
            case 3:
                return TAG_FAVORITES;
            default:
                return TAG_PROFILE;
        }
    }

    @NonNull
    private Fragment createFragment(int index) {
        switch (index) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new CalendarFragment();
            case 3:
                return new FavoritesFragment();
            default:
                return new ProfileFragment();
        }
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentTabIndex != 0) {
                    binding.bottomBar.selectTabAt(0, true);
                } else {
                    moveTaskToBack(true);
                }
            }
        });
    }
}