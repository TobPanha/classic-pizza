package com.scentify.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scentify.app.R;
import com.scentify.app.data.local.SessionManager;
import com.scentify.app.databinding.ActivitySignUpBinding;
import com.scentify.app.ui.onboarding.OnboardingActivity;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCreate.setOnClickListener(v -> attemptSignUp());
        binding.buttonBackToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }

    private void attemptSignUp() {
        binding.textError.setVisibility(android.view.View.GONE);
        String email = getText(binding.editEmail);
        String password = getText(binding.editPassword);
        String confirm = getText(binding.editConfirm);
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
            showError(getString(R.string.auth_error_empty_fields));
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(getString(R.string.auth_error_credentials));
            return;
        }
        if (!password.equals(confirm)) {
            showError(getString(R.string.auth_error_password_match));
            return;
        }
        sessionManager.register(email, password);
        Intent intent = new Intent(this, OnboardingActivity.class);
        intent.putExtra(OnboardingActivity.EXTRA_FORCE_FLOW, true);
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
