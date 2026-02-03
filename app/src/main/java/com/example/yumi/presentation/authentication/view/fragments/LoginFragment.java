package com.example.yumi.presentation.authentication.view.fragments;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumi.data.config.SharedPreferencesKeysConfig;
import com.example.yumi.databinding.FragmentLoginBinding;

import java.util.Objects;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            binding = FragmentLoginBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentLoginBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.registerText.setOnClickListener(v -> navigateToRegister());
        binding.loginButton.setOnClickListener(v -> navigateToHome());
        binding.continueAsGuest.setOnClickListener(v -> navigateToHome());
    }

    private void navigateToRegister() {
        if (isAdded() && !isDetached()) {
            try {
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
            } catch (Exception e) {
                Log.e("Bug", Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    private void navigateToHome() {
        if (isAdded() && !isDetached()) {
            try {
                SharedPreferences prefs = requireActivity()
                        .getSharedPreferences(SharedPreferencesKeysConfig.PREF_NAME, MODE_PRIVATE);
                prefs.edit().putBoolean(SharedPreferencesKeysConfig.KEY_LOGGED_IN, true).apply();

                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeBaseActivity());

                requireActivity().finish();
            } catch (Exception e) {
                Log.e("Bug", Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Auth", "On destroy");
    }
}