package com.scentify.app.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.List;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey
    @NonNull
    private final String id;
    private final String name;
    private final String brand;
    private final double price;
    private final String imageUrl;
    private final List<String> tags;

    public Product(@NonNull String id,
                   String name,
                   String brand,
                   double price,
                   String imageUrl,
                   List<String> tags) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.tags = tags == null ? Collections.emptyList() : tags;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean matchesKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        String lowered = keyword.trim().toLowerCase();
        return name.toLowerCase().contains(lowered) || brand.toLowerCase().contains(lowered);
    }
}
