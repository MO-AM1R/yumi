package com.example.yumi.presentation.authentication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yumi.R;
import com.example.yumi.databinding.FragmentRegisterBinding;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.authentication.presenter.SignUpPresenter;
import com.google.android.material.snackbar.Snackbar;

public class RegisterFragment extends Fragment implements AuthContract.SignUpView {

    private FragmentRegisterBinding binding;
    private SignUpPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SignUpPresenter(requireActivity().getApplication(), this);
        presenter.attachView(this);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.registerButton.setOnClickListener(v -> {
            String username = binding.usernameTextField.getText() != null
                    ? binding.usernameTextField.getText().toString().trim()
                    : "";
            String email = binding.emailTextField.getText() != null
                    ? binding.emailTextField.getText().toString().trim()
                    : "";
            String password = binding.passwordTextField.getText() != null
                    ? binding.passwordTextField.getText().toString().trim()
                    : "";

            presenter.signUp(username, email, password);
        });

        binding.googleIcon.setOnClickListener(v ->
                presenter.signUpWithGoogle(requireActivity())
        );

        binding.loginText.setOnClickListener(v -> navigateToLogin());
    }

    private void navigateToLogin() {
        if (isAdded() && !isDetached()) {
            try {
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.popBackStack();
            } catch (Exception e) {
                showError("Navigation error");
            }
        }
    }

    @Override
    public void showUsernameError(String error) {
        binding.usernameTextField.setError(error);
        binding.usernameTextField.requestFocus();
    }

    @Override
    public void showEmailError(String error) {
        binding.emailTextField.setError(error);
        if (binding.usernameTextField.getError() == null) {
            binding.emailTextField.requestFocus();
        }
    }

    @Override
    public void showPasswordError(String error) {
        binding.passwordTextField.setError(error);
        if (binding.usernameTextField.getError() == null && binding.emailTextField.getError() == null) {
            binding.passwordTextField.requestFocus();
        }
    }

    @Override
    public void clearErrors() {
        binding.usernameTextField.setError(null);
        binding.emailTextField.setError(null);
        binding.passwordTextField.setError(null);
    }

    @Override
    public void onSignUpSuccess() {
        if (isAdded() && !isDetached()) {
            try {
                showSuccess(getString(R.string.register_success));
                navigateToLogin();
            } catch (Exception e) {
                showError("Navigation error");
            }
        }
    }

    @Override
    public void showLoading() {
        binding.registerButton.setEnabled(false);
        binding.googleIcon.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.registerButton.setEnabled(true);
        binding.googleIcon.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark, null))
                .show();
    }

    @Override
    public void showSuccess(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark, null))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
        binding = null;
    }
}