package com.scentify.app.utils;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Utility methods for presenting product tags in UI.
 */
public final class TagFormatter {
    private static final String[] KNOWN_PREFIXES = new String[]{
            "fragrance_",
            "occasion_",
            "brand_",
            "budget_",
            "intensity_"
    };

    private TagFormatter() {
    }

    public static String format(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return "";
        }
        String cleaned = tag.trim().toLowerCase(Locale.getDefault());
        for (String prefix : KNOWN_PREFIXES) {
            if (cleaned.startsWith(prefix)) {
                cleaned = cleaned.substring(prefix.length());
                break;
            }
        }
        cleaned = cleaned.replace("_", " ").trim();
        if (cleaned.isEmpty()) {
            return "";
        }
        return cleaned.substring(0, 1).toUpperCase(Locale.getDefault()) + cleaned.substring(1);
    }
}
