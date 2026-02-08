package com.example.yumi.presentation.home.view.fragments;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentProfileBinding;
import com.example.yumi.utils.LocaleHelper;
import com.example.yumi.utils.ThemeHelper;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private boolean isSpinnerInitialized = false;


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

        setupLanguageSpinner();
        setupDarkModeSwitch();
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

                String selectedLang = getLangCodeFromPosition(pos);

                if (LocaleHelper.isSameLanguage(requireContext(), selectedLang)) {
                    return;
                }

                LocaleHelper.saveLanguage(requireContext(), selectedLang);
                requireActivity().recreate();
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

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, checked) -> {
            ThemeHelper.saveTheme(requireContext(), checked);
            AppCompatDelegate.setDefaultNightMode(
                    checked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }
}