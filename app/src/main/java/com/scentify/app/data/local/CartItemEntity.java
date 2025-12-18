package com.scentify.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItemEntity {
    @PrimaryKey
    @NonNull
    private final String productId;
    private final int quantity;

    public CartItemEntity(@NonNull String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @NonNull
    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
