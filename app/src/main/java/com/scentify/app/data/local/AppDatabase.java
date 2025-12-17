package com.scentify.app.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.scentify.app.data.model.Product;

@Database(entities = {Product.class, CartItemEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract CartDao cartDao();
}
