package com.scentify.app.quiz;

import java.util.Collections;
import java.util.List;

public class QuizResult {
    private final String profileMessage;
    private final List<String> profileTags;
    private final List<QuizProductMatch> matches;
    private final int topMatchPercentage;

    public QuizResult(String profileMessage,
                      List<String> profileTags,
                      List<QuizProductMatch> matches,
                      int topMatchPercentage) {
        this.profileMessage = profileMessage;
        this.profileTags = profileTags == null ? Collections.emptyList() : profileTags;
        this.matches = matches == null ? Collections.emptyList() : matches;
        this.topMatchPercentage = topMatchPercentage;
    }

    public String getProfileMessage() {
        return profileMessage;
    }

    public List<String> getProfileTags() {
        return profileTags;
    }

    public List<QuizProductMatch> getMatches() {
        return matches;
    }

    public int getTopMatchPercentage() {
        return topMatchPercentage;
    }
}
