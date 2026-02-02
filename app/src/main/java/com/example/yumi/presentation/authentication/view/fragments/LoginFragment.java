package com.example.yumi.presentation.authentication.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumi.R;
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

        binding.registerText.setOnClickListener(v -> {
            if (isAdded() && !isDetached()) {
                try {
                    NavController navController = Navigation.findNavController(binding.getRoot());
                    navController.navigate(
                            LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
                } catch (Exception e) {
                    Log.e("Bug", Objects.requireNonNull(e.getMessage()));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Auth", "On destroy");
    }
}