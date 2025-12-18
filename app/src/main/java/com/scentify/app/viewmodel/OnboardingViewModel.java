package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.UserPreference;
import com.scentify.app.data.repository.ScentRepository;

import java.util.ArrayList;
import java.util.List;

public class OnboardingViewModel extends ViewModel {
    private final ScentRepository repository;
    private final MutableLiveData<List<String>> fragranceChoices = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> occasionChoices = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> brandChoices = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> budgetChoice = new MutableLiveData<>("");
    private final MediatorLiveData<UserPreference> preview = new MediatorLiveData<>();

    public OnboardingViewModel(ScentRepository repository) {
        this.repository = repository;
        UserPreference existing = repository.getCurrentPreference();
        fragranceChoices.setValue(new ArrayList<>(existing.getFragranceTypes()));
        occasionChoices.setValue(new ArrayList<>(existing.getOccasions()));
        brandChoices.setValue(new ArrayList<>(existing.getBrandCategories()));
        budgetChoice.setValue(existing.getBudgetRange());
        preview.setValue(existing);
    }

    public LiveData<UserPreference> getPreview() {
        return preview;
    }

    public void toggleFragrance(String option, boolean selected) {
        toggleSelection(fragranceChoices, option, selected);
    }

    public void toggleOccasion(String option, boolean selected) {
        toggleSelection(occasionChoices, option, selected);
    }

    public void toggleBrandCategory(String option, boolean selected) {
        toggleSelection(brandChoices, option, selected);
    }

    public void selectBudget(String budget) {
        budgetChoice.setValue(budget);
        updatePreview();
    }

    public void savePreference() {
        UserPreference preference = preview.getValue();
        if (preference == null) {
            preference = buildPreference();
        }
        repository.savePreference(preference);
        repository.markOnboardingComplete();
    }

    public boolean hasCompletedOnboarding() {
        return repository.isOnboardingComplete();
    }

    public void markOnboardingCompleteWithoutSaving() {
        repository.markOnboardingComplete();
    }

    private void toggleSelection(MutableLiveData<List<String>> liveData, String value, boolean selected) {
        List<String> current = liveData.getValue();
        if (current == null) {
            current = new ArrayList<>();
        } else {
            current = new ArrayList<>(current);
        }

        if (selected) {
            if (!current.contains(value)) {
                current.add(value);
            }
        } else {
            current.remove(value);
        }
        liveData.setValue(current);
        updatePreview();
    }

    private void updatePreview() {
        preview.setValue(buildPreference());
    }

    private UserPreference buildPreference() {
        List<String> fragrances = fragranceChoices.getValue();
        List<String> occasions = occasionChoices.getValue();
        List<String> brands = brandChoices.getValue();
        String budget = budgetChoice.getValue();
        return new UserPreference(
                fragrances == null ? new ArrayList<>() : fragrances,
                occasions == null ? new ArrayList<>() : occasions,
                brands == null ? new ArrayList<>() : brands,
                budget == null ? "" : budget
        );
    }
}
