package com.scentify.app.utils;

import com.scentify.app.data.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SampleDataProvider {
    private SampleDataProvider() {
    }

    public static List<Product> provideProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(
                "p1",
                "Sunlit Citrus",
                "Aurora Scents",
                65.0,
                "",
                Arrays.asList("fragrance_citrus", "fragrance_fresh", "occasion_daily", "brand_indie", "budget_50_100", "intensity_light")
        ));
        products.add(new Product(
                "p2",
                "Velvet Bloom",
                "Maison Fleur",
                120.0,
                "",
                Arrays.asList("fragrance_floral", "occasion_evening", "brand_luxury", "budget_100_150", "intensity_moderate")
        ));
        products.add(new Product(
                "p3",
                "Midnight Oud",
                "Nocturne Atelier",
                180.0,
                "",
                Arrays.asList("fragrance_oriental", "occasion_special", "brand_luxury", "budget_150_plus", "intensity_strong")
        ));
        products.add(new Product(
                "p4",
                "Cedar Trails",
                "Terra Notes",
                75.0,
                "",
                Arrays.asList("fragrance_woody", "occasion_office", "brand_indie", "budget_50_100", "intensity_strong")
        ));
        products.add(new Product(
                "p5",
                "Garden Whisper",
                "Classic Essence",
                55.0,
                "",
                Arrays.asList("fragrance_floral", "occasion_daily", "brand_classic", "budget_under_50", "intensity_moderate")
        ));
        products.add(new Product(
                "p6",
                "Amber Veil",
                "Eclipse Parfums",
                95.0,
                "",
                Arrays.asList("fragrance_oriental", "occasion_evening", "brand_indie", "budget_50_100", "intensity_strong")
        ));
        products.add(new Product(
                "p7",
                "Sea Glass",
                "New Coast",
                48.0,
                "",
                Arrays.asList("fragrance_fresh", "occasion_sporty", "brand_clean", "budget_under_50", "intensity_light")
        ));
        products.add(new Product(
                "p8",
                "Urban Vetiver",
                "Metro Blend",
                82.0,
                "",
                Arrays.asList("fragrance_woody", "occasion_office", "brand_clean", "budget_50_100", "intensity_moderate")
        ));
        products.add(new Product(
                "p9",
                "Rose Reverie",
                "Maison Fleur",
                140.0,
                "",
                Arrays.asList("fragrance_floral", "occasion_special", "brand_luxury", "budget_100_150", "intensity_moderate")
        ));
        products.add(new Product(
                "p10",
                "Cocoa Ember",
                "Gourmande Lab",
                90.0,
                "",
                Arrays.asList("fragrance_gourmand", "occasion_evening", "brand_indie", "budget_50_100", "intensity_strong")
        ));
        products.add(new Product(
                "p11",
                "Citrus Atelier",
                "Classic Essence",
                52.0,
                "",
                Arrays.asList("fragrance_citrus", "occasion_daily", "brand_classic", "budget_50_100", "intensity_light")
        ));
        products.add(new Product(
                "p12",
                "Ivy Clean",
                "Pure Bloom",
                60.0,
                "",
                Arrays.asList("fragrance_fresh", "occasion_office", "brand_clean", "budget_50_100", "intensity_light")
        ));
        products.add(new Product(
                "p13",
                "Golden Fig",
                "Gourmande Lab",
                110.0,
                "",
                Arrays.asList("fragrance_gourmand", "occasion_special", "brand_indie", "budget_100_150", "intensity_strong")
        ));
        products.add(new Product(
                "p14",
                "Atlas Smoke",
                "Nocturne Atelier",
                160.0,
                "",
                Arrays.asList("fragrance_woody", "fragrance_oriental", "occasion_evening", "brand_luxury", "budget_150_plus", "intensity_strong")
        ));
        products.add(new Product(
                "p15",
                "Petal Mist",
                "Aurora Scents",
                58.0,
                "",
                Arrays.asList("fragrance_floral", "occasion_daily", "brand_indie", "budget_50_100", "intensity_moderate")
        ));
        products.add(new Product(
                "p16",
                "Cobalt Rush",
                "Metro Blend",
                72.0,
                "",
                Arrays.asList("fragrance_fresh", "occasion_sporty", "brand_clean", "budget_50_100", "intensity_light")
        ));
        products.add(new Product(
                "p17",
                "Lavender Drift",
                "Pure Bloom",
                68.0,
                "",
                Arrays.asList("fragrance_floral", "fragrance_fresh", "occasion_office", "brand_clean", "budget_50_100", "intensity_moderate")
        ));
        products.add(new Product(
                "p18",
                "Spice Market",
                "Terra Notes",
                88.0,
                "",
                Arrays.asList("fragrance_oriental", "fragrance_gourmand", "occasion_special", "brand_classic", "budget_50_100", "intensity_strong")
        ));
        return products;
    }
}
