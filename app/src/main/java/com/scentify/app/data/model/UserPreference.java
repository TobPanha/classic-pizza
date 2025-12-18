package com.scentify.app.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserPreference {
    private final List<String> fragranceTypes;
    private final List<String> occasions;
    private final List<String> brandCategories;
    private final String budgetRange;

    public UserPreference(List<String> fragranceTypes,
                          List<String> occasions,
                          List<String> brandCategories,
                          String budgetRange) {
        this.fragranceTypes = fragranceTypes == null ? new ArrayList<>() : new ArrayList<>(fragranceTypes);
        this.occasions = occasions == null ? new ArrayList<>() : new ArrayList<>(occasions);
        this.brandCategories = brandCategories == null ? new ArrayList<>() : new ArrayList<>(brandCategories);
        this.budgetRange = budgetRange == null ? "" : budgetRange;
    }

    public List<String> getFragranceTypes() {
        return Collections.unmodifiableList(fragranceTypes);
    }

    public List<String> getOccasions() {
        return Collections.unmodifiableList(occasions);
    }

    public List<String> getBrandCategories() {
        return Collections.unmodifiableList(brandCategories);
    }

    public String getBudgetRange() {
        return budgetRange;
    }

    public boolean hasSelections() {
        return !fragranceTypes.isEmpty() || !occasions.isEmpty() || !brandCategories.isEmpty() || !budgetRange.isEmpty();
    }
}
