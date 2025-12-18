package com.scentify.app.quiz;

import android.text.TextUtils;

import com.scentify.app.data.model.Product;
import com.scentify.app.data.model.UserPreference;
import com.scentify.app.utils.PreferenceRanker;
import com.scentify.app.utils.TagFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class QuizEngine {
    private QuizEngine() {
    }

    public static QuizResult evaluate(Map<String, Integer> tagScores,
                                      List<Product> products,
                                      UserPreference preference) {
        if (tagScores == null || tagScores.isEmpty()) {
            return new QuizResult("", Collections.emptyList(), Collections.emptyList(), 0);
        }
        Map<String, Integer> orderedScores = new LinkedHashMap<>();
        int totalScore = 0;
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(tagScores.entrySet());
        entries.sort((first, second) -> Integer.compare(second.getValue(), first.getValue()));
        for (Map.Entry<String, Integer> entry : entries) {
            orderedScores.put(entry.getKey(), entry.getValue());
            totalScore += Math.max(entry.getValue(), 0);
        }
        if (totalScore == 0) {
            totalScore = entries.size();
        }
        List<String> topTags = extractTopTags(orderedScores);
        List<QuizProductMatch> matches = calculateMatches(orderedScores, products, totalScore, preference);
        int topMatchPercentage = matches.isEmpty() ? 0 : matches.get(0).getMatchPercentage();
        return new QuizResult(buildProfileMessage(topTags), convertTagsToLabels(topTags), matches, topMatchPercentage);
    }

    private static List<String> extractTopTags(Map<String, Integer> orderedScores) {
        List<String> top = new ArrayList<>();
        if (orderedScores == null) {
            return top;
        }
        int count = 0;
        for (String tag : orderedScores.keySet()) {
            top.add(tag);
            count++;
            if (count >= 3) {
                break;
            }
        }
        return top;
    }

    private static List<QuizProductMatch> calculateMatches(Map<String, Integer> orderedScores,
                                                           List<Product> products,
                                                           int totalScore,
                                                           UserPreference preference) {
        if (products == null) {
            return Collections.emptyList();
        }
        Map<String, Integer> preferenceOrdering = buildPreferenceOrdering(products, preference);
        List<QuizProductMatch> matches = new ArrayList<>();
        Set<String> tags = orderedScores.keySet();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Set<String> productTags = new LinkedHashSet<>(product.getTags());
            List<String> matchedTags = new ArrayList<>();
            int matchedScore = 0;
            for (String tag : tags) {
                if (productTags.contains(tag)) {
                    matchedTags.add(tag);
                    matchedScore += Math.max(1, orderedScores.get(tag));
                }
            }
            int percentage = totalScore == 0 ? 0 : Math.round((matchedScore / (float) totalScore) * 100f);
            percentage = Math.max(10, Math.min(100, percentage));
            matches.add(new QuizProductMatch(product, percentage, matchedTags));
        }
        matches.sort(new QuizMatchComparator(preferenceOrdering));
        if (matches.size() > 3) {
            return new ArrayList<>(matches.subList(0, 3));
        }
        return matches;
    }

    private static Map<String, Integer> buildPreferenceOrdering(List<Product> products, UserPreference preference) {
        Map<String, Integer> ordering = new LinkedHashMap<>();
        List<Product> ranked = PreferenceRanker.rankProducts(products, preference, null);
        for (int i = 0; i < ranked.size(); i++) {
            ordering.put(ranked.get(i).getId(), i);
        }
        return ordering;
    }

    private static String buildProfileMessage(List<String> topTags) {
        if (topTags == null || topTags.isEmpty()) {
            return "";
        }
        String primary = TagFormatter.format(topTags.get(0));
        if (TextUtils.isEmpty(primary)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(primary);
        if (topTags.size() >= 2) {
            String secondary = TagFormatter.format(topTags.get(1));
            if (!TextUtils.isEmpty(secondary)) {
                if (isOccasion(topTags.get(1))) {
                    builder.append(" for ").append(secondary);
                } else if (isBudget(topTags.get(1))) {
                    builder.append(" within ").append(secondary);
                } else if (isIntensity(topTags.get(1))) {
                    builder.append(" with ").append(secondary).append(" intensity");
                } else {
                    builder.append(" with ").append(secondary);
                }
            }
        }
        if (topTags.size() >= 3) {
            String tertiary = TagFormatter.format(topTags.get(2));
            if (!TextUtils.isEmpty(tertiary)) {
                builder.append(" • ").append(tertiary);
            }
        }
        return builder.toString();
    }

    private static List<String> convertTagsToLabels(List<String> tags) {
        List<String> labels = new ArrayList<>();
        if (tags == null) {
            return labels;
        }
        for (String tag : tags) {
            String label = TagFormatter.format(tag);
            if (!TextUtils.isEmpty(label)) {
                labels.add(label);
            }
        }
        return labels;
    }

    private static boolean isOccasion(String tag) {
        return tag != null && tag.startsWith("occasion_");
    }

    private static boolean isBudget(String tag) {
        return tag != null && tag.startsWith("budget_");
    }

    private static boolean isIntensity(String tag) {
        return tag != null && tag.startsWith("intensity_");
    }

    private static class QuizMatchComparator implements Comparator<QuizProductMatch> {
        private final Map<String, Integer> preferenceOrder;

        QuizMatchComparator(Map<String, Integer> preferenceOrder) {
            this.preferenceOrder = preferenceOrder == null ? Collections.emptyMap() : preferenceOrder;
        }

        @Override
        public int compare(QuizProductMatch first, QuizProductMatch second) {
            int percentage = Integer.compare(second.getMatchPercentage(), first.getMatchPercentage());
            if (percentage != 0) {
                return percentage;
            }
            int matchedTagsCompare = Integer.compare(
                    second.getMatchingTags().size(),
                    first.getMatchingTags().size()
            );
            if (matchedTagsCompare != 0) {
                return matchedTagsCompare;
            }
            int firstPref = preferenceOrder.getOrDefault(first.getProduct().getId(), Integer.MAX_VALUE);
            int secondPref = preferenceOrder.getOrDefault(second.getProduct().getId(), Integer.MAX_VALUE);
            if (firstPref != secondPref) {
                return Integer.compare(firstPref, secondPref);
            }
            String firstId = first.getProduct().getId();
            String secondId = second.getProduct().getId();
            return firstId.compareTo(secondId);
        }
    }
}
