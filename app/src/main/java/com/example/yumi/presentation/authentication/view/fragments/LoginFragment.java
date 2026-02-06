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
import com.example.yumi.databinding.FragmentLoginBinding;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.authentication.presenter.LoginPresenter;
import com.google.android.material.snackbar.Snackbar;


public class LoginFragment extends Fragment implements AuthContract.LoginView {

    private FragmentLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new LoginPresenter(requireActivity().getApplication(), this);
        presenter.attachView(this);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailTextField.getText() != null
                    ? binding.emailTextField.getText().toString().trim()
                    : "";
            String password = binding.passwordTextField.getText() != null
                    ? binding.passwordTextField.getText().toString().trim()
                    : "";

            presenter.login(email, password);
        });

        binding.googleIcon.setOnClickListener(v ->
                presenter.loginWithGoogle(requireActivity())
        );

        binding.registerText.setOnClickListener(v -> navigateToRegister());

        //TODO: continue as guest
        binding.continueAsGuest.setOnClickListener(v -> {});
    }

    private void navigateToRegister() {
        if (isAdded() && !isDetached()) {
            try {
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                );
            } catch (Exception e) {
                showError("Navigation error");
            }
        }
    }

    @Override
    public void showEmailError(String error) {
        binding.emailTextField.setError(error);
        binding.emailTextField.requestFocus();
    }

    @Override
    public void showPasswordError(String error) {
        binding.passwordTextField.setError(error);
        if (binding.emailTextField.getError() == null) {
            binding.passwordTextField.requestFocus();
        }
    }

    @Override
    public void clearErrors() {
        binding.emailTextField.setError(null);
        binding.passwordTextField.setError(null);
    }

    @Override
    public void onLoginSuccess() {
        if (isAdded() && !isDetached()) {
            try {
                showSuccess(getString(R.string.login_success));
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeBaseActivity()
                );
                requireActivity().finish();
            } catch (Exception e) {
                showError("Navigation error");
            }
        }
    }

    @Override
    public void showLoading() {
        binding.loginButton.setEnabled(false);
        binding.googleIcon.setEnabled(false);
        binding.continueAsGuest.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.loginButton.setEnabled(true);
        binding.googleIcon.setEnabled(true);
        binding.continueAsGuest.setEnabled(true);
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