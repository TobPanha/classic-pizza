package com.scentify.app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.data.local.SessionManager;
import com.scentify.app.data.model.UserPreference;
import com.scentify.app.databinding.FragmentSettingsBinding;
import com.scentify.app.ui.auth.SignInActivity;
import com.scentify.app.ui.onboarding.OnboardingActivity;
import com.scentify.app.viewmodel.ScentViewModelFactory;
import com.scentify.app.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ScentifyApplication app = (ScentifyApplication) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository()))
                .get(SettingsViewModel.class);
        sessionManager = new SessionManager(requireContext());

        String email = sessionManager.getEmail();
        binding.textAccountEmail.setText(email.isEmpty() ? getString(R.string.settings_not_set) : email);

        binding.buttonEditPreferences.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), OnboardingActivity.class);
            intent.putExtra(OnboardingActivity.EXTRA_FORCE_FLOW, true);
            startActivity(intent);
        });

        binding.buttonClearPreferences.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.settings_clear_title)
                        .setMessage(R.string.settings_clear_message)
                        .setPositiveButton(R.string.settings_clear_confirm, (dialog, which) -> viewModel.clearPreferences())
                        .setNegativeButton(android.R.string.cancel, null)
                        .show());

        binding.buttonClearSaved.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.settings_clear_saved)
                        .setMessage(R.string.settings_clear_saved_message)
                        .setPositiveButton(R.string.settings_clear_confirm, (dialog, which) -> viewModel.clearSavedItems())
                        .setNegativeButton(android.R.string.cancel, null)
                        .show());
        binding.buttonLogout.setOnClickListener(v -> {
            sessionManager.signOut();
            navigateToAuth();
        });

        viewModel.getPreferences().observe(getViewLifecycleOwner(), this::renderPreferences);
    }

    private void renderPreferences(UserPreference preference) {
        if (preference == null || !preference.hasSelections()) {
            binding.textPreferencesSummary.setText(R.string.settings_no_preferences);
            return;
        }
        String summary = getString(R.string.settings_summary_template,
                joinList(preference.getFragranceTypes()),
                joinList(preference.getOccasions()),
                joinList(preference.getBrandCategories()),
                preference.getBudgetRange().isEmpty() ? getString(R.string.settings_any_budget) : preference.getBudgetRange());
        binding.textPreferencesSummary.setText(summary);
    }

    private String joinList(java.util.List<String> values) {
        if (values == null || values.isEmpty()) {
            return getString(R.string.settings_not_set);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            builder.append(values.get(i));
            if (i < values.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private void navigateToAuth() {
        Intent intent = new Intent(requireContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
