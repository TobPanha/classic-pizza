package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.Product;
import com.scentify.app.data.model.UserPreference;
import com.scentify.app.data.repository.ScentRepository;
import com.scentify.app.utils.PreferenceRanker;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final ScentRepository repository;
    private final MediatorLiveData<List<Product>> rankedProducts = new MediatorLiveData<>();
    private List<Product> latestProducts = new ArrayList<>();
    private UserPreference latestPreference;

    public HomeViewModel(ScentRepository repository) {
        this.repository = repository;
        rankedProducts.setValue(new ArrayList<>());
        rankedProducts.addSource(repository.observeProducts(), products -> {
            latestProducts = products == null ? new ArrayList<>() : products;
            updateRanking();
        });
        rankedProducts.addSource(repository.observePreferences(), preference -> {
            latestPreference = preference;
            updateRanking();
        });
    }

    public LiveData<List<Product>> getRankedProducts() {
        return rankedProducts;
    }

    private void updateRanking() {
        rankedProducts.setValue(PreferenceRanker.rankProducts(latestProducts, latestPreference, null));
    }
}
