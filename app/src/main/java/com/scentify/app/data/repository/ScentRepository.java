package com.scentify.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.scentify.app.data.local.AppDatabase;
import com.scentify.app.data.local.CartDao;
import com.scentify.app.data.local.CartItemEntity;
import com.scentify.app.data.local.CartItemWithProduct;
import com.scentify.app.data.local.DatabaseProvider;
import com.scentify.app.data.local.PreferenceStorage;
import com.scentify.app.data.local.ProductDao;
import com.scentify.app.data.model.CartItem;
import com.scentify.app.data.model.Product;
import com.scentify.app.data.model.UserPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScentRepository {
    private final ProductDao productDao;
    private final CartDao cartDao;
    private final PreferenceStorage preferenceStorage;
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
    private final MediatorLiveData<UserPreference> preferenceLiveData = new MediatorLiveData<>();

    public ScentRepository(Context context) {
        AppDatabase database = DatabaseProvider.getInstance(context);
        this.productDao = database.productDao();
        this.cartDao = database.cartDao();
        this.preferenceStorage = new PreferenceStorage(context);
        preferenceLiveData.setValue(preferenceStorage.getUserPreference());
    }

    public LiveData<List<Product>> observeProducts() {
        return productDao.observeProducts();
    }

    public LiveData<Product> observeProduct(String productId) {
        return productDao.observeProductById(productId);
    }

    public LiveData<List<CartItem>> observeCartItems() {
        return Transformations.map(cartDao.observeCartItems(), this::toCartItems);
    }

    public void addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }
        ioExecutor.execute(() -> {
            CartItemEntity existing = cartDao.getCartItem(product.getId());
            int updatedQuantity = quantity;
            if (existing != null) {
                updatedQuantity = existing.getQuantity() + quantity;
            }
            cartDao.upsert(new CartItemEntity(product.getId(), updatedQuantity));
        });
    }

    public void updateCartQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(productId);
            return;
        }
        ioExecutor.execute(() -> cartDao.updateQuantity(productId, quantity));
    }

    public void removeFromCart(String productId) {
        ioExecutor.execute(() -> cartDao.removeItem(productId));
    }

    public void clearCart() {
        ioExecutor.execute(cartDao::clearCart);
    }

    public LiveData<UserPreference> observePreferences() {
        return preferenceLiveData;
    }

    public void savePreference(UserPreference preference) {
        preferenceStorage.saveUserPreference(preference);
        preferenceLiveData.postValue(preference);
    }

    public void clearPreferences() {
        preferenceStorage.clear();
        preferenceLiveData.postValue(new UserPreference(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), ""));
    }

    public boolean hasPreferences() {
        return preferenceStorage.hasPreferences();
    }

    public boolean isOnboardingComplete() {
        return preferenceStorage.isOnboardingComplete();
    }

    public void markOnboardingComplete() {
        preferenceStorage.markOnboardingComplete();
    }

    public UserPreference getCurrentPreference() {
        return preferenceStorage.getUserPreference();
    }

    private List<CartItem> toCartItems(List<CartItemWithProduct> raw) {
        List<CartItem> items = new ArrayList<>();
        if (raw == null) {
            return items;
        }
        for (CartItemWithProduct withProduct : raw) {
            if (withProduct.product != null && withProduct.item != null) {
                items.add(new CartItem(withProduct.product, withProduct.item.getQuantity()));
            }
        }
        return items;
    }
}
