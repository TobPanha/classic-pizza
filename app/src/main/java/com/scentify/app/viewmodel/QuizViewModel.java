package com.scentify.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.scentify.app.data.model.Product;
import com.scentify.app.data.model.UserPreference;
import com.scentify.app.data.repository.ScentRepository;
import com.scentify.app.quiz.QuizAnswer;
import com.scentify.app.quiz.QuizEngine;
import com.scentify.app.quiz.QuizQuestion;
import com.scentify.app.quiz.QuizQuestionBank;
import com.scentify.app.quiz.QuizResult;
import com.scentify.app.quiz.QuizUiState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuizViewModel extends ViewModel {
    private final List<QuizQuestion> questions;
    private final MediatorLiveData<QuizUiState> uiState = new MediatorLiveData<>();
    private final Map<String, Integer> tagScores = new LinkedHashMap<>();
    private final List<Product> latestProducts = new ArrayList<>();
    private UserPreference latestPreference;
    private int currentQuestionIndex = -1;

    public QuizViewModel(ScentRepository repository) {
        this.questions = Collections.unmodifiableList(QuizQuestionBank.provideQuestions());
        uiState.setValue(QuizUiState.intro(questions.size()));
        uiState.addSource(repository.observeProducts(), products -> {
            latestProducts.clear();
            if (products != null) {
                latestProducts.addAll(products);
            }
        });
        uiState.addSource(repository.observePreferences(), preference -> latestPreference = preference);
    }

    public LiveData<QuizUiState> getUiState() {
        return uiState;
    }

    public void beginQuiz() {
        resetProgress();
        moveToQuestion(0);
    }

    public void selectAnswer(QuizAnswer answer) {
        if (answer == null || currentQuestionIndex < 0 || currentQuestionIndex >= questions.size()) {
            return;
        }
        incrementScore(answer.getTag());
        if (currentQuestionIndex == questions.size() - 1) {
            QuizResult result = QuizEngine.evaluate(tagScores, latestProducts, latestPreference);
            uiState.setValue(QuizUiState.result(result, questions.size()));
        } else {
            moveToQuestion(currentQuestionIndex + 1);
        }
    }

    public void retakeQuiz() {
        resetProgress();
        uiState.setValue(QuizUiState.intro(questions.size()));
    }

    private void moveToQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            return;
        }
        currentQuestionIndex = index;
        uiState.setValue(QuizUiState.question(questions.get(index), index, questions.size()));
    }

    private void incrementScore(String tag) {
        if (tag == null) {
            return;
        }
        int current = tagScores.containsKey(tag) ? tagScores.get(tag) : 0;
        tagScores.put(tag, current + 1);
    }

    private void resetProgress() {
        currentQuestionIndex = -1;
        tagScores.clear();
    }
}
