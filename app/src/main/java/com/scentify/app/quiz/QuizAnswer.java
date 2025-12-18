package com.scentify.app.quiz;

import androidx.annotation.StringRes;

public class QuizAnswer {
    private final int labelResId;
    private final String tag;

    public QuizAnswer(@StringRes int labelResId, String tag) {
        this.labelResId = labelResId;
        this.tag = tag;
    }

    @StringRes
    public int getLabelResId() {
        return labelResId;
    }

    public String getTag() {
        return tag;
    }
}
