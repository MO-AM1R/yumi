package com.example.yumi.presentation.home.view.activities;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.yumi.R;
import com.example.yumi.databinding.ActivityHomeBaseBinding;
import com.example.yumi.presentation.home.view.fragments.CalendarFragment;
import com.example.yumi.presentation.home.view.fragments.FavoritesFragment;
import com.example.yumi.presentation.home.view.fragments.HomeFragment;
import com.example.yumi.presentation.home.view.fragments.ProfileFragment;
import com.example.yumi.presentation.home.view.fragments.SearchFragment;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import java.util.Stack;
import nl.joery.animatedbottombar.AnimatedBottomBar;


public class HomeBaseActivity extends AppCompatActivity implements NavigationCallback {
    private ActivityHomeBaseBinding binding;
    private Fragment activeFragment;
    private int currentTabIndex = 0;
    private final Stack<Fragment> fragmentStack = new Stack<>();
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

        setupSystemBars();
        applyWindowInsets();
        setupBottomBar();
        setupBackPressHandler();
    }

    private void setupSystemBars() {
        Window window = getWindow();

        window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
        window.setNavigationBarColor(getResources().getColor(R.color.card, getTheme()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, false);
        }
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets displayCutout = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout());

            int topInset = Math.max(systemBars.top, displayCutout.top);
            int bottomInset = systemBars.bottom;

            binding.fragmentContainer.setPadding(0, topInset, 0, 0);

            binding.bottomBar.setPadding(
                    binding.bottomBar.getPaddingLeft(),
                    binding.bottomBar.getPaddingTop(),
                    binding.bottomBar.getPaddingRight(),
                    bottomInset
            );

            return WindowInsetsCompat.CONSUMED;
        });
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

    private void setupBottomBar() {
        binding.bottomBar.selectTabAt(currentTabIndex, true);

        binding.bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int oldIndex, @Nullable AnimatedBottomBar.Tab oldTab,
                                      int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                if (currentTabIndex != newIndex) {
                    clearFragmentStack();
                    currentTabIndex = newIndex;
                    switchFragment(newIndex);
                }
            }

            @Override
            public void onTabReselected(int index, @NonNull AnimatedBottomBar.Tab tab) {
                popToRoot();
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
            case 0: return TAG_HOME;
            case 1: return TAG_SEARCH;
            case 2: return TAG_CALENDAR;
            case 3: return TAG_FAVORITES;
            default: return TAG_PROFILE;
        }
    }

    @NonNull
    private Fragment createFragment(int index) {
        switch (index) {
            case 0: return new HomeFragment();
            case 1: return new SearchFragment();
            case 2: return new CalendarFragment();
            case 3: return new FavoritesFragment();
            default: return new ProfileFragment();
        }
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!fragmentStack.isEmpty()) {
                    popFragment();
                } else if (currentTabIndex != 0) {
                    binding.bottomBar.selectTabAt(0, true);
                } else {
                    moveTaskToBack(true);
                }
            }
        });
    }


    @Override
    public void navigateToFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );

        if (activeFragment != null) {
            transaction.hide(activeFragment);
            fragmentStack.push(activeFragment);
        }


        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.commit();

        activeFragment = fragment;
        hideBottomBar();
    }

    @Override
    public void popFragment() {
        if (!fragmentStack.isEmpty()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );

            transaction.remove(activeFragment);

            Fragment previousFragment = fragmentStack.pop();
            transaction.show(previousFragment);
            transaction.commit();

            activeFragment = previousFragment;

            if (fragmentStack.isEmpty()) {
                showBottomBar();
            }
        }
    }

    @Override
    public void popToRoot() {
        if (fragmentStack.isEmpty()) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.remove(activeFragment);

        while (fragmentStack.size() > 1) {
            Fragment fragment = fragmentStack.pop();
            transaction.remove(fragment);
        }

        Fragment rootFragment = fragmentStack.pop();
        transaction.show(rootFragment);
        transaction.commit();

        activeFragment = rootFragment;
        showBottomBar();
    }

    @Override
    public void clearStackAndNavigate(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (activeFragment != null) {
            transaction.remove(activeFragment);
        }

        while (!fragmentStack.isEmpty()) {
            Fragment f = fragmentStack.pop();
            transaction.remove(f);
        }

        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.commit();

        activeFragment = fragment;
    }

    @Override
    public void showBottomBar() {
        binding.bottomBar.setVisibility(VISIBLE);
    }

    @Override
    public void hideBottomBar() {
        binding.bottomBar.setVisibility(GONE);
    }

    private void clearFragmentStack() {
        if (fragmentStack.isEmpty()) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        while (!fragmentStack.isEmpty()) {
            Fragment fragment = fragmentStack.pop();
            transaction.remove(fragment);
        }

        transaction.commit();
    }
}