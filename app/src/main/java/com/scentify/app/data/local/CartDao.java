package com.scentify.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CartDao {
    @Transaction
    @Query("SELECT * FROM cart_items")
    LiveData<List<CartItemWithProduct>> observeCartItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(CartItemEntity entity);

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    CartItemEntity getCartItem(String productId);

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    void updateQuantity(String productId, int quantity);

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    void removeItem(String productId);

    @Query("DELETE FROM cart_items")
    void clearCart();
}
