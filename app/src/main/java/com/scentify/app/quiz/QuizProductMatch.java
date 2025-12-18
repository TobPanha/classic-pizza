package com.scentify.app.quiz;

import com.scentify.app.data.model.Product;

import java.util.List;

public class QuizProductMatch {
    private final Product product;
    private final int matchPercentage;
    private final List<String> matchingTags;

    public QuizProductMatch(Product product, int matchPercentage, List<String> matchingTags) {
        this.product = product;
        this.matchPercentage = matchPercentage;
        this.matchingTags = matchingTags;
    }

    public Product getProduct() {
        return product;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public List<String> getMatchingTags() {
        return matchingTags;
    }
}
