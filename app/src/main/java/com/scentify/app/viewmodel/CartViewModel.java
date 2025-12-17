package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.CartItem;
import com.scentify.app.data.repository.ScentRepository;

import java.util.List;

public class CartViewModel extends ViewModel {
    private final ScentRepository repository;
    private final LiveData<List<CartItem>> cartItems;
    private final LiveData<Double> totalPrice;

    public CartViewModel(ScentRepository repository) {
        this.repository = repository;
        this.cartItems = repository.observeCartItems();
        this.totalPrice = Transformations.map(cartItems, items -> {
            double total = 0.0;
            if (items != null) {
                for (CartItem item : items) {
                    total += item.getLineTotal();
                }
            }
            return total;
        });
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public LiveData<Double> getTotalPrice() {
        return totalPrice;
    }

    public void increaseQuantity(CartItem item) {
        repository.updateCartQuantity(item.getProduct().getId(), item.getQuantity() + 1);
    }

    public void decreaseQuantity(CartItem item) {
        repository.updateCartQuantity(item.getProduct().getId(), item.getQuantity() - 1);
    }

    public void removeItem(CartItem item) {
        repository.removeFromCart(item.getProduct().getId());
    }

    public void clearCart() {
        repository.clearCart();
    }
}
