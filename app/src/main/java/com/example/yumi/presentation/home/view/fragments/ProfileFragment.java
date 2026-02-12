package com.example.yumi.presentation.home.view.fragments;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentProfileBinding;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.presentation.authentication.view.activities.AuthenticationActivity;
import com.example.yumi.presentation.home.contract.ProfileContract;
import com.example.yumi.presentation.home.presenter.ProfilePresenter;
import com.example.yumi.utils.LocaleHelper;
import com.example.yumi.utils.ThemeHelper;


public class ProfileFragment extends Fragment implements ProfileContract.View {
    private FragmentProfileBinding binding;
    private boolean isSpinnerInitialized = false;
    private ProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (view == null) {
            binding = FragmentProfileBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentProfileBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ProfilePresenter(requireActivity().getApplicationContext());
        presenter.attachView(this);

        setupLanguageSpinner();
        setupDarkModeSwitch();
        setupSyncDataButtons();
        presenter.loadUserDetails();

        binding.cardLogout.setOnClickListener(v -> presenter.logout());
    }

    private void setupSyncDataButtons() {
        binding.cardSync.setOnClickListener(v -> presenter.syncData());
        binding.cardRetrieveData.setOnClickListener(v -> presenter.retrieveData());
    }

    private void setupLanguageSpinner() {
        Spinner spinner = binding.spinnerLanguage;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.languages_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String currentLang = LocaleHelper.getCurrentLanguage(requireContext());
        int position = getIndexForLanguage(currentLang);
        spinner.setSelection(position);

        spinner.post(() -> isSpinnerInitialized = true);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (!isSpinnerInitialized) {
                    return;
                }

                presenter.onLanguageChanged(getLangCodeFromPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private int getIndexForLanguage(String langCode) {
        if (LocaleHelper.LANG_ARABIC.equalsIgnoreCase(langCode)) {
            return 1;
        }
        return 0;
    }

    private String getLangCodeFromPosition(int position) {
        if (position == 1) {
            return LocaleHelper.LANG_ARABIC;
        }
        return LocaleHelper.LANG_ENGLISH;
    }

    private void setupDarkModeSwitch() {
        Boolean savedTheme = ThemeHelper.getSavedTheme(requireContext());
        boolean isDark;

        if (savedTheme == null) {
            int nightMode =
                    getResources().getConfiguration().uiMode
                            & Configuration.UI_MODE_NIGHT_MASK;

            isDark = nightMode == Configuration.UI_MODE_NIGHT_YES;
        } else {
            isDark = savedTheme;
        }

        binding.switchDarkMode.setChecked(isDark);

        binding.switchDarkMode.setOnCheckedChangeListener(
                (buttonView, checked) -> presenter.onModeChanged(checked));
    }

    @Override
    public void showUserDetails(User user) {
        binding.userAvatar.setText(user.getDisplayName().substring(0, 2).toUpperCase());
        binding.userNameText.setText(user.getDisplayName());
        binding.emailTextView.setText(user.getEmail());
        binding.favoriteCountText.setText(String.valueOf(user.getFavoriteMealIds().size()));
    }

    @Override
    public void showUserPlannedMealsCounter(int plannedMeals) {
        binding.plannedCountText.setText(String.valueOf(plannedMeals));
    }

    @Override
    public void showUserFavoriteMealsCounter(int favoriteMeals) {
        binding.favoriteCountText.setText(String.valueOf(favoriteMeals));
    }

    @Override
    public void resetLanguage() {
        requireActivity().recreate();
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent(
                requireActivity(),
                AuthenticationActivity.class
        );

        intent.putExtra("SPLASH", false);
        startActivity(intent);
    }

    @Override
    public void onDataRetrievedSuccess() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
    }

    @Override
    public void showLoading() {
        binding.loading.setVisibility(VISIBLE);
        binding.loading.setIndeterminate(true);
        binding.getRoot().setClickable(false);
        binding.getRoot().setFocusable(false);
        toggleView(GONE);
    }

    @Override
    public void hideLoading() {
        binding.loading.setVisibility(GONE);
        binding.loading.setIndeterminate(false);
        binding.getRoot().setClickable(true);
        binding.getRoot().setFocusable(false);
        toggleView(VISIBLE);
    }

    private void toggleView(int visibility) {
        binding.plannedCountText.setVisibility(visibility);
        binding.favoriteCountText.setVisibility(visibility);
    }

    @Override
    public void showError(String message) {

    }
}