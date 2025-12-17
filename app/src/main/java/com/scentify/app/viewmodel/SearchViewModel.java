package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.Product;
import com.scentify.app.data.model.UserPreference;
import com.scentify.app.data.repository.ScentRepository;
import com.scentify.app.utils.PreferenceRanker;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private final ScentRepository repository;
    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final MediatorLiveData<List<Product>> searchResults = new MediatorLiveData<>();
    private List<Product> latestProducts = new ArrayList<>();
    private UserPreference latestPreference;

    public SearchViewModel(ScentRepository repository) {
        this.repository = repository;
        searchResults.setValue(new ArrayList<>());
        searchResults.addSource(repository.observeProducts(), products -> {
            latestProducts = products == null ? new ArrayList<>() : products;
            updateResults();
        });
        searchResults.addSource(repository.observePreferences(), preference -> {
            latestPreference = preference;
            updateResults();
        });
        searchResults.addSource(query, value -> updateResults());
    }

    public LiveData<List<Product>> getResults() {
        return searchResults;
    }

    public void setQuery(String keyword) {
        query.setValue(keyword == null ? "" : keyword);
    }

    public LiveData<String> getQuery() {
        return query;
    }

    private void updateResults() {
        String keyword = query.getValue();
        searchResults.setValue(PreferenceRanker.rankProducts(latestProducts, latestPreference, keyword));
    }
}
