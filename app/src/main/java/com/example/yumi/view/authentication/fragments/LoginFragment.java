package com.example.yumi.view.authentication.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumi.R;
import com.example.yumi.databinding.FragmentLoginBinding;
import com.example.yumi.databinding.FragmentSplashBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null){
            binding = FragmentLoginBinding.inflate(inflater);
            view = binding.getRoot();
        }else{
            binding = FragmentLoginBinding.bind(view);
        }

        return view;
    }
}