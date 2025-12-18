package com.scentify.app.quiz;

import androidx.annotation.StringRes;

import java.util.List;

public class QuizQuestion {
    private final String id;
    private final int promptResId;
    private final List<QuizAnswer> answers;

    public QuizQuestion(String id, @StringRes int promptResId, List<QuizAnswer> answers) {
        this.id = id;
        this.promptResId = promptResId;
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    @StringRes
    public int getPromptResId() {
        return promptResId;
    }

    public List<QuizAnswer> getAnswers() {
        return answers;
    }
}
