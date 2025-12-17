package com.scentify.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.scentify.app.data.model.UserPreference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferenceStorage {
    private static final String PREF_NAME = "scentify_preferences";
    private static final String KEY_FRAGRANCE = "pref_fragrance";
    private static final String KEY_OCCASION = "pref_occasion";
    private static final String KEY_BRAND = "pref_brand";
    private static final String KEY_BUDGET = "pref_budget";
    private static final String KEY_ONBOARDING_COMPLETE = "pref_onboarding_complete";

    private final SharedPreferences sharedPreferences;

    public PreferenceStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserPreference(UserPreference preference) {
        sharedPreferences.edit()
                .putStringSet(KEY_FRAGRANCE, new HashSet<>(preference.getFragranceTypes()))
                .putStringSet(KEY_OCCASION, new HashSet<>(preference.getOccasions()))
                .putStringSet(KEY_BRAND, new HashSet<>(preference.getBrandCategories()))
                .putString(KEY_BUDGET, preference.getBudgetRange())
                .putBoolean(KEY_ONBOARDING_COMPLETE, true)
                .apply();
    }

    public UserPreference getUserPreference() {
        List<String> fragrance = toList(sharedPreferences.getStringSet(KEY_FRAGRANCE, new HashSet<>()));
        List<String> occasion = toList(sharedPreferences.getStringSet(KEY_OCCASION, new HashSet<>()));
        List<String> brand = toList(sharedPreferences.getStringSet(KEY_BRAND, new HashSet<>()));
        String budget = sharedPreferences.getString(KEY_BUDGET, "");
        return new UserPreference(fragrance, occasion, brand, budget == null ? "" : budget);
    }

    public boolean hasPreferences() {
        return !sharedPreferences.getStringSet(KEY_FRAGRANCE, new HashSet<>()).isEmpty()
                || !sharedPreferences.getStringSet(KEY_OCCASION, new HashSet<>()).isEmpty()
                || !sharedPreferences.getStringSet(KEY_BRAND, new HashSet<>()).isEmpty()
                || (sharedPreferences.getString(KEY_BUDGET, "") != null && !sharedPreferences.getString(KEY_BUDGET, "").isEmpty());
    }

    public boolean isOnboardingComplete() {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETE, false);
    }

    public void markOnboardingComplete() {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETE, true).apply();
    }

    public void clear() {
        sharedPreferences.edit()
                .remove(KEY_FRAGRANCE)
                .remove(KEY_OCCASION)
                .remove(KEY_BRAND)
                .remove(KEY_BUDGET)
                .apply();
    }

    private List<String> toList(Set<String> items) {
        if (items == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(items);
    }
}
