package com.example.yumi.presentation.authentication.view.fragments;
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

import com.example.yumi.R;
import com.example.yumi.databinding.FragmentRegisterBinding;

import java.util.Objects;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null){
            binding = FragmentRegisterBinding.inflate(inflater);
            view = binding.getRoot();
        }else{
            binding = FragmentRegisterBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginText.setOnClickListener(v -> {
            if (isAdded() && !isDetached()) {
                try {
                    NavController navController = Navigation.findNavController(binding.getRoot());
                    navController.navigate(R.id.action_registerFragment_to_loginFragment);
                } catch (Exception e) {
                    Log.e("Bug", Objects.requireNonNull(e.getMessage()));
                }
            }
        });
    }
}