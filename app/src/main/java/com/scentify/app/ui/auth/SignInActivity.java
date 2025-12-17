package com.scentify.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scentify.app.databinding.ActivitySignInBinding;
import com.scentify.app.data.local.PreferenceStorage;
import com.scentify.app.data.local.SessionManager;
import com.scentify.app.ui.MainActivity;
import com.scentify.app.ui.onboarding.OnboardingActivity;
import com.scentify.app.R;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private SessionManager sessionManager;
    private PreferenceStorage preferenceStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        preferenceStorage = new PreferenceStorage(this);
        if (sessionManager.isSignedIn()) {
            navigateForward();
            return;
        }

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSignIn.setOnClickListener(v -> attemptSignIn());
        binding.buttonCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void attemptSignIn() {
        binding.textError.setVisibility(android.view.View.GONE);
        String email = getText(binding.editEmail);
        String password = getText(binding.editPassword);
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError(getString(R.string.auth_error_empty_fields));
            return;
        }
        if (!sessionManager.hasAccount()) {
            showError(getString(R.string.auth_error_credentials));
            return;
        }
        boolean success = sessionManager.signIn(email, password);
        if (success) {
            navigateForward();
        } else {
            showError(getString(R.string.auth_error_credentials));
        }
    }

    private void navigateForward() {
        Intent intent;
        if (preferenceStorage.isOnboardingComplete()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, OnboardingActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private String getText(android.widget.TextView view) {
        return view.getText() == null ? "" : view.getText().toString().trim();
    }

    private void showError(String message) {
        binding.textError.setText(message);
        binding.textError.setVisibility(android.view.View.VISIBLE);
    }
}
