package com.scentify.app.utils;

import android.text.TextUtils;

import com.scentify.app.data.model.Product;
import com.scentify.app.data.model.UserPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class PreferenceRanker {
    private PreferenceRanker() {
    }

    public static List<Product> rankProducts(List<Product> products, UserPreference preference, String keyword) {
        if (products == null) {
            return new ArrayList<>();
        }
        List<Product> copy = new ArrayList<>(products);
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasPreference = preference != null && preference.hasSelections();

        if (!hasPreference && !hasKeyword) {
            return copy;
        }

        String loweredKeyword = hasKeyword ? keyword.trim().toLowerCase(Locale.getDefault()) : "";
        List<String> desiredTags = hasPreference ? buildDesiredTags(preference) : Collections.emptyList();

        List<ScoredProduct> scored = new ArrayList<>();
        for (int i = 0; i < copy.size(); i++) {
            Product product = copy.get(i);
            if (hasKeyword && !product.matchesKeyword(loweredKeyword)) {
                continue;
            }
            int preferenceScore = hasPreference ? calculatePreferenceScore(product, desiredTags, preference.getBudgetRange()) : 0;
            int keywordScore = hasKeyword ? keywordScore(product, loweredKeyword) : 0;
            scored.add(new ScoredProduct(product, preferenceScore, keywordScore, i));
        }

        if (!hasPreference) {
            scored.sort((first, second) -> {
                int keywordCompare = Integer.compare(second.keywordScore, first.keywordScore);
                if (keywordCompare != 0) {
                    return keywordCompare;
                }
                return first.originalIndex - second.originalIndex;
            });
        } else {
            scored.sort((first, second) -> {
                int prefCompare = Integer.compare(second.preferenceScore, first.preferenceScore);
                if (prefCompare != 0) {
                    return prefCompare;
                }
                int keywordCompare = Integer.compare(second.keywordScore, first.keywordScore);
                if (keywordCompare != 0) {
                    return keywordCompare;
                }
                return first.originalIndex - second.originalIndex;
            });
        }

        List<Product> ranked = new ArrayList<>();
        for (ScoredProduct scoredProduct : scored) {
            ranked.add(scoredProduct.product);
        }
        return ranked;
    }

    private static int calculatePreferenceScore(Product product, List<String> desiredTags, String budgetRange) {
        int matches = 0;
        List<String> productTags = product.getTags();
        for (String desired : desiredTags) {
            if (productTags.contains(desired)) {
                matches++;
            }
        }
        if (!TextUtils.isEmpty(budgetRange)) {
            String budgetTag = mapBudgetToTag(budgetRange);
            if (!TextUtils.isEmpty(budgetTag) && productTags.contains(budgetTag)) {
                matches++;
            }
        }
        return matches;
    }

    private static List<String> buildDesiredTags(UserPreference preference) {
        List<String> tags = new ArrayList<>();
        for (String type : preference.getFragranceTypes()) {
            tags.add("fragrance_" + normalize(type));
        }
        for (String occasion : preference.getOccasions()) {
            String occasionTag = mapOccasionTag(occasion);
            if (!TextUtils.isEmpty(occasionTag)) {
                tags.add(occasionTag);
            }
        }
        for (String brand : preference.getBrandCategories()) {
            String brandTag = mapBrandTag(brand);
            if (!TextUtils.isEmpty(brandTag)) {
                tags.add(brandTag);
            }
        }
        return tags;
    }

    private static String normalize(String source) {
        if (source == null) {
            return "";
        }
        return source.trim()
                .toLowerCase(Locale.getDefault())
                .replace(" ", "_")
                .replace("+", "plus")
                .replace("-", "_");
    }

    private static String mapBudgetToTag(String budget) {
        if (budget == null) {
            return "";
        }
        switch (budget) {
            case "Under $50":
                return "budget_under_50";
            case "$50 - $100":
                return "budget_50_100";
            case "$100 - $150":
                return "budget_100_150";
            case "$150+":
                return "budget_150_plus";
            default:
                return "";
        }
    }

    private static String mapOccasionTag(String occasion) {
        if (occasion == null) {
            return "";
        }
        switch (occasion) {
            case "Daily Wear":
                return "occasion_daily";
            case "Evening Out":
                return "occasion_evening";
            case "Office":
                return "occasion_office";
            case "Special Events":
                return "occasion_special";
            case "Sporty":
                return "occasion_sporty";
            default:
                return "";
        }
    }

    private static String mapBrandTag(String brand) {
        if (brand == null) {
            return "";
        }
        switch (brand) {
            case "Luxury Houses":
                return "brand_luxury";
            case "Indie Brands":
                return "brand_indie";
            case "Classic Icons":
                return "brand_classic";
            case "Clean Beauty":
                return "brand_clean";
            default:
                return "";
        }
    }

    private static int keywordScore(Product product, String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return 0;
        }
        String name = product.getName().toLowerCase(Locale.getDefault());
        String brand = product.getBrand().toLowerCase(Locale.getDefault());
        if (name.startsWith(keyword) || brand.startsWith(keyword)) {
            return 2;
        }
        if (name.contains(keyword) || brand.contains(keyword)) {
            return 1;
        }
        return 0;
    }

    private static class ScoredProduct {
        final Product product;
        final int preferenceScore;
        final int keywordScore;
        final int originalIndex;

        ScoredProduct(Product product, int preferenceScore, int keywordScore, int originalIndex) {
            this.product = product;
            this.preferenceScore = preferenceScore;
            this.keywordScore = keywordScore;
            this.originalIndex = originalIndex;
        }
    }
}
