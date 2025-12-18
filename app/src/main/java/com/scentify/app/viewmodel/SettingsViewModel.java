package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.UserPreference;
import com.scentify.app.data.repository.ScentRepository;

public class SettingsViewModel extends ViewModel {
    private final ScentRepository repository;
    private final LiveData<UserPreference> preferences;

    public SettingsViewModel(ScentRepository repository) {
        this.repository = repository;
        this.preferences = repository.observePreferences();
    }

    public LiveData<UserPreference> getPreferences() {
        return preferences;
    }

    public void clearPreferences() {
        repository.clearPreferences();
    }

    public boolean hasPreferences() {
        return repository.hasPreferences();
    }

    public void clearSavedItems() {
        repository.clearCart();
    }
}
