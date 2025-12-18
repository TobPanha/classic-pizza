package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.Product;
import com.scentify.app.data.repository.ScentRepository;

public class ProductDetailViewModel extends ViewModel {
    private final ScentRepository repository;
    private final LiveData<Product> product;

    public ProductDetailViewModel(ScentRepository repository, String productId) {
        this.repository = repository;
        this.product = repository.observeProduct(productId);
    }

    public LiveData<Product> getProduct() {
        return product;
    }

    public void addToCart(Product product, int quantity) {
        repository.addToCart(product, quantity);
    }
}
