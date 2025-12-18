package com.scentify.app.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.data.model.UserPreference;
import com.scentify.app.databinding.ActivityOnboardingBinding;
import com.scentify.app.ui.MainActivity;
import com.scentify.app.viewmodel.OnboardingViewModel;
import com.scentify.app.viewmodel.ScentViewModelFactory;

import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    public static final String EXTRA_FORCE_FLOW = "extra_force_flow";

    private ActivityOnboardingBinding binding;
    private OnboardingViewModel viewModel;
    private boolean syncingChips = false;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ScentifyApplication app = (ScentifyApplication) getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository()))
                .get(OnboardingViewModel.class);

        boolean forceFlow = getIntent().getBooleanExtra(EXTRA_FORCE_FLOW, false);
        if (!forceFlow && viewModel.hasCompletedOnboarding()) {
            navigateToHome();
            return;
        }

        setupChips();
        observePreference();
        bindActions();
        showPage(0);
    }

    private void setupChips() {
        populateChipGroup(binding.groupFragrance, R.array.fragrance_types, viewModel::toggleFragrance);
        populateChipGroup(binding.groupOccasions, R.array.occasion_types, viewModel::toggleOccasion);
        populateChipGroup(binding.groupBrands, R.array.brand_categories, viewModel::toggleBrandCategory);
        populateBudgetChips();
    }

    private void observePreference() {
        viewModel.getPreview().observe(this, this::renderPreference);
    }

    private void bindActions() {
        binding.buttonSkip.setOnClickListener(v -> {
            viewModel.markOnboardingCompleteWithoutSaving();
            navigateToHome();
        });
        binding.buttonContinue.setOnClickListener(v -> {
            if (currentPage == 0) {
                showPage(1);
            } else if (currentPage == 1) {
                showPage(2);
            } else {
                viewModel.savePreference();
                navigateToHome();
            }
        });
    }

    private void populateChipGroup(ChipGroup chipGroup,
                                   @ArrayRes int arrayRes,
                                   ChipToggleListener listener) {
        String[] options = getResources().getStringArray(arrayRes);
        for (String option : options) {
            Chip chip = createChoiceChip(option);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!syncingChips) {
                    listener.onToggle(option, isChecked);
                }
            });
            chipGroup.addView(chip);
        }
    }

    private void populateBudgetChips() {
        binding.groupBudget.setSingleSelection(true);
        binding.groupBudget.setSelectionRequired(false);
        String[] options = getResources().getStringArray(R.array.budget_ranges);
        for (String option : options) {
            Chip chip = createChoiceChip(option);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (syncingChips) {
                    return;
                }
                if (isChecked) {
                    viewModel.selectBudget(option);
                } else {
                    UserPreference current = viewModel.getPreview().getValue();
                    if (current != null && option.equals(current.getBudgetRange())) {
                        viewModel.selectBudget("");
                    }
                }
            });
            binding.groupBudget.addView(chip);
        }
    }

    private Chip createChoiceChip(String label) {
        Chip chip = new Chip(this);
        chip.setText(label);
        chip.setCheckable(true);
        chip.setEnsureMinTouchTargetSize(false);
        chip.setChipBackgroundColorResource(R.color.chip_background_color);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.chip_text_color));
        chip.setTextAppearance(R.style.TextAppearance_Scentify_Body);
        chip.setRippleColorResource(R.color.ripple_accent);
        chip.setChipStrokeWidth(0f);
        chip.setChipCornerRadius(getResources().getDimension(R.dimen.scentify_chip_radius));
        chip.setCheckedIconVisible(false);
        return chip;
    }

    private void renderPreference(UserPreference preference) {
        syncingChips = true;
        applySelections(binding.groupFragrance, preference.getFragranceTypes());
        applySelections(binding.groupOccasions, preference.getOccasions());
        applySelections(binding.groupBrands, preference.getBrandCategories());
        applyBudgetSelection(preference.getBudgetRange());
        binding.textSelectionSummary.setText(buildSummary(preference));
        renderSummaryChips(preference);
        syncingChips = false;
    }

    private void renderSummaryChips(UserPreference preference) {
        binding.groupSummarySelections.removeAllViews();
        if (preference == null || !preference.hasSelections()) {
            Chip chip = createChoiceChip(getString(R.string.settings_no_preferences));
            chip.setChecked(true);
            chip.setEnabled(false);
            binding.groupSummarySelections.addView(chip);
            return;
        }
        for (String fragrance : preference.getFragranceTypes()) {
            binding.groupSummarySelections.addView(createSummaryChip(fragrance));
        }
        for (String occasion : preference.getOccasions()) {
            binding.groupSummarySelections.addView(createSummaryChip(occasion));
        }
        for (String brand : preference.getBrandCategories()) {
            binding.groupSummarySelections.addView(createSummaryChip(brand));
        }
        if (!preference.getBudgetRange().isEmpty()) {
            binding.groupSummarySelections.addView(createSummaryChip(preference.getBudgetRange()));
        }
    }

    private Chip createSummaryChip(String label) {
        Chip chip = createChoiceChip(label);
        chip.setChecked(true);
        chip.setEnabled(false);
        chip.setClickable(false);
        return chip;
    }

    private void applySelections(ChipGroup chipGroup, List<String> selections) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setChecked(selections.contains(chip.getText().toString()));
        }
    }

    private void applyBudgetSelection(String budget) {
        for (int i = 0; i < binding.groupBudget.getChildCount(); i++) {
            Chip chip = (Chip) binding.groupBudget.getChildAt(i);
            chip.setChecked(chip.getText().toString().equals(budget));
        }
    }

    private String buildSummary(UserPreference preference) {
        if (!preference.hasSelections()) {
            return "No preferences selected yet.";
        }
        return "Fragrances: " + preference.getFragranceTypes().size() +
                " • Occasions: " + preference.getOccasions().size() +
                " • Brands: " + preference.getBrandCategories().size() +
                " • Budget: " + (preference.getBudgetRange().isEmpty() ? "Any" : preference.getBudgetRange());
    }

    private void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showPage(int page) {
        currentPage = page;
        binding.layoutIntro.setVisibility(page == 0 ? View.VISIBLE : View.GONE);
        binding.scrollPreferences.setVisibility(page == 1 ? View.VISIBLE : View.GONE);
        binding.layoutSummary.setVisibility(page == 2 ? View.VISIBLE : View.GONE);
        binding.buttonSkip.setVisibility(page == 1 ? View.VISIBLE : View.GONE);
        if (page == 0) {
            binding.buttonContinue.setText(R.string.onboarding_continue);
        } else if (page == 1) {
            binding.buttonContinue.setText(R.string.onboarding_continue);
        } else {
            binding.buttonContinue.setText(R.string.onboarding_start_exploring);
        }
    }

    interface ChipToggleListener {
        void onToggle(String label, boolean selected);
    }
}
