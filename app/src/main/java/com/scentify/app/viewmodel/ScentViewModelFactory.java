package com.scentify.app.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.scentify.app.data.repository.ScentRepository;

public class ScentViewModelFactory implements ViewModelProvider.Factory {
    private final ScentRepository repository;
    private final String productId;

    public ScentViewModelFactory(ScentRepository repository) {
        this(repository, null);
    }

    public ScentViewModelFactory(ScentRepository repository, String productId) {
        this.repository = repository;
        this.productId = productId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OnboardingViewModel.class)) {
            return (T) new OnboardingViewModel(repository);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository);
        } else if (modelClass.isAssignableFrom(ProductDetailViewModel.class)) {
            if (productId == null) {
                throw new IllegalArgumentException("Product id is required for ProductDetailViewModel");
            }
            return (T) new ProductDetailViewModel(repository, productId);
        } else if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(repository);
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
