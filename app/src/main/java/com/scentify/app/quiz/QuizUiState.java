package com.scentify.app.quiz;

import androidx.annotation.Nullable;

public class QuizUiState {
    public enum Screen {
        INTRO,
        QUESTION,
        RESULT
    }

    private final Screen screen;
    private final QuizQuestion currentQuestion;
    private final int questionIndex;
    private final int totalQuestions;
    private final QuizResult quizResult;

    private QuizUiState(Screen screen,
                        QuizQuestion currentQuestion,
                        int questionIndex,
                        int totalQuestions,
                        QuizResult quizResult) {
        this.screen = screen;
        this.currentQuestion = currentQuestion;
        this.questionIndex = questionIndex;
        this.totalQuestions = totalQuestions;
        this.quizResult = quizResult;
    }

    public static QuizUiState intro(int totalQuestions) {
        return new QuizUiState(Screen.INTRO, null, -1, totalQuestions, null);
    }

    public static QuizUiState question(QuizQuestion question, int questionIndex, int totalQuestions) {
        return new QuizUiState(Screen.QUESTION, question, questionIndex, totalQuestions, null);
    }

    public static QuizUiState result(QuizResult result, int totalQuestions) {
        return new QuizUiState(Screen.RESULT, null, totalQuestions - 1, totalQuestions, result);
    }

    public Screen getScreen() {
        return screen;
    }

    @Nullable
    public QuizQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    @Nullable
    public QuizResult getQuizResult() {
        return quizResult;
    }
}
