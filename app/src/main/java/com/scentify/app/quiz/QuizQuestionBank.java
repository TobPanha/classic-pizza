package com.scentify.app.quiz;

import com.scentify.app.R;

import java.util.Arrays;
import java.util.List;

public final class QuizQuestionBank {
    private QuizQuestionBank() {
    }

    public static List<QuizQuestion> provideQuestions() {
        return Arrays.asList(
                new QuizQuestion(
                        "when",
                        R.string.quiz_question_wear_time,
                        Arrays.asList(
                                new QuizAnswer(R.string.quiz_answer_wear_daily, "occasion_daily"),
                                new QuizAnswer(R.string.quiz_answer_wear_office, "occasion_office"),
                                new QuizAnswer(R.string.quiz_answer_wear_date, "occasion_evening"),
                                new QuizAnswer(R.string.quiz_answer_wear_party, "occasion_special")
                        )
                ),
                new QuizQuestion(
                        "scent",
                        R.string.quiz_question_attracts,
                        Arrays.asList(
                                new QuizAnswer(R.string.quiz_answer_scent_floral, "fragrance_floral"),
                                new QuizAnswer(R.string.quiz_answer_scent_woody, "fragrance_woody"),
                                new QuizAnswer(R.string.quiz_answer_scent_fresh, "fragrance_fresh"),
                                new QuizAnswer(R.string.quiz_answer_scent_spicy, "fragrance_oriental")
                        )
                ),
                new QuizQuestion(
                        "vibe",
                        R.string.quiz_question_vibe,
                        Arrays.asList(
                                new QuizAnswer(R.string.quiz_answer_vibe_clean, "fragrance_fresh"),
                                new QuizAnswer(R.string.quiz_answer_vibe_elegant, "fragrance_floral"),
                                new QuizAnswer(R.string.quiz_answer_vibe_bold, "fragrance_oriental"),
                                new QuizAnswer(R.string.quiz_answer_vibe_warm, "fragrance_woody")
                        )
                ),
                new QuizQuestion(
                        "intensity",
                        R.string.quiz_question_intensity,
                        Arrays.asList(
                                new QuizAnswer(R.string.quiz_answer_intensity_light, "intensity_light"),
                                new QuizAnswer(R.string.quiz_answer_intensity_moderate, "intensity_moderate"),
                                new QuizAnswer(R.string.quiz_answer_intensity_strong, "intensity_strong")
                        )
                ),
                new QuizQuestion(
                        "budget",
                        R.string.quiz_question_budget,
                        Arrays.asList(
                                new QuizAnswer(R.string.quiz_answer_budget_affordable, "budget_under_50"),
                                new QuizAnswer(R.string.quiz_answer_budget_mid, "budget_50_100"),
                                new QuizAnswer(R.string.quiz_answer_budget_premium, "budget_150_plus")
                        )
                )
        );
    }
}
