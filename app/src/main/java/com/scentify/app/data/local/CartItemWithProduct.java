package com.scentify.app.data.local;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.scentify.app.data.model.Product;

public class CartItemWithProduct {
    @Embedded
    public CartItemEntity item;

    @Relation(
            parentColumn = "productId",
            entityColumn = "id"
    )
    public Product product;
}
