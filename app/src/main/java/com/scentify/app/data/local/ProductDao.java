package com.scentify.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.scentify.app.data.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    LiveData<List<Product>> observeProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<Product> products);

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    LiveData<Product> observeProductById(String id);

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    Product getProductSync(String id);
}
