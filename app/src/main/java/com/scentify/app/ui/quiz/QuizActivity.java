package com.scentify.app.ui.quiz;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.databinding.ActivityQuizBinding;
import com.scentify.app.databinding.ItemQuizOptionBinding;
import com.scentify.app.quiz.QuizAnswer;
import com.scentify.app.quiz.QuizQuestion;
import com.scentify.app.quiz.QuizResult;
import com.scentify.app.quiz.QuizUiState;
import com.scentify.app.viewmodel.QuizViewModel;
import com.scentify.app.viewmodel.ScentViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private ActivityQuizBinding binding;
    private QuizViewModel viewModel;
    private QuizMatchAdapter matchAdapter;
    private final List<MaterialCardView> answerCards = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarQuiz.setNavigationOnClickListener(v -> finish());
        binding.progressMatch.setMax(100);

        matchAdapter = new QuizMatchAdapter();
        binding.recyclerQuizMatches.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerQuizMatches.setAdapter(matchAdapter);

        binding.buttonStartQuiz.setOnClickListener(v -> viewModel.beginQuiz());
        binding.buttonRetake.setOnClickListener(v -> viewModel.retakeQuiz());
        binding.buttonFinish.setOnClickListener(v -> finish());

        ScentifyApplication app = (ScentifyApplication) getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository()))
                .get(QuizViewModel.class);
        viewModel.getUiState().observe(this, this::renderState);
    }

    private void renderState(QuizUiState state) {
        if (state == null) {
            return;
        }
        QuizUiState.Screen screen = state.getScreen();
        binding.layoutIntro.setVisibility(screen == QuizUiState.Screen.INTRO ? View.VISIBLE : View.GONE);
        binding.layoutQuestion.setVisibility(screen == QuizUiState.Screen.QUESTION ? View.VISIBLE : View.GONE);
        binding.layoutResult.setVisibility(screen == QuizUiState.Screen.RESULT ? View.VISIBLE : View.GONE);
        if (screen == QuizUiState.Screen.QUESTION) {
            renderQuestion(state);
        } else if (screen == QuizUiState.Screen.RESULT) {
            renderResult(state.getQuizResult());
        }
    }

    private void renderQuestion(QuizUiState state) {
        QuizQuestion question = state.getCurrentQuestion();
        if (question == null) {
            return;
        }
        binding.textQuizProgress.setText(getString(R.string.quiz_progress_format, state.getQuestionIndex() + 1, state.getTotalQuestions()));
        binding.progressQuiz.setMax(state.getTotalQuestions());
        binding.progressQuiz.setProgressCompat(state.getQuestionIndex() + 1, true);
        binding.textQuestionPrompt.setText(question.getPromptResId());
        binding.groupQuizOptions.removeAllViews();
        answerCards.clear();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (QuizAnswer answer : question.getAnswers()) {
            ItemQuizOptionBinding optionBinding = ItemQuizOptionBinding.inflate(inflater, binding.groupQuizOptions, false);
            optionBinding.textOptionLabel.setText(answer.getLabelResId());
            MaterialCardView card = (MaterialCardView) optionBinding.getRoot();
            card.setOnClickListener(v -> {
                disableInteractions();
                highlightSelection(card);
                viewModel.selectAnswer(answer);
            });
            binding.groupQuizOptions.addView(card);
            answerCards.add(card);
        }
    }

    private void renderResult(@Nullable QuizResult result) {
        if (result == null) {
            return;
        }
        String summary = TextUtils.isEmpty(result.getProfileMessage())
                ? getString(R.string.quiz_result_empty_profile)
                : result.getProfileMessage();
        binding.textProfileSummary.setText(summary);
        renderProfileChips(result.getProfileTags());
        binding.textMatchValue.setText(String.format(Locale.getDefault(), "%d%%", result.getTopMatchPercentage()));
        binding.progressMatch.setProgressCompat(result.getTopMatchPercentage(), true);
        matchAdapter.submit(result.getMatches());
    }

    private void renderProfileChips(List<String> tags) {
        binding.chipProfileTags.removeAllViews();
        List<String> source = tags == null || tags.isEmpty() ? new ArrayList<>() : tags;
        if (source.isEmpty()) {
            source.add(getString(R.string.quiz_result_empty_profile));
        }
        for (String tag : source) {
            binding.chipProfileTags.addView(createTagChip(tag));
        }
    }

    private Chip createTagChip(String label) {
        Chip chip = new Chip(this);
        chip.setText(label);
        chip.setCheckable(true);
        chip.setChecked(true);
        chip.setClickable(false);
        chip.setEnsureMinTouchTargetSize(false);
        chip.setTextAppearance(R.style.TextAppearance_Scentify_Caption);
        chip.setChipBackgroundColorResource(R.color.chip_background_color);
        chip.setTextColor(getColorStateList(R.color.chip_text_color));
        chip.setChipCornerRadius(getResources().getDimension(R.dimen.scentify_chip_radius));
        chip.setChipStrokeWidth(0f);
        chip.setCheckedIconVisible(false);
        return chip;
    }

    private void disableInteractions() {
        for (MaterialCardView card : answerCards) {
            card.setEnabled(false);
        }
    }

    private void highlightSelection(MaterialCardView selected) {
        int strokeWidth = getResources().getDimensionPixelSize(R.dimen.scentify_chip_radius) / 2;
        if (strokeWidth <= 0) {
            float density = getResources().getDisplayMetrics().density;
            strokeWidth = (int) (density * 2);
        }
        for (MaterialCardView card : answerCards) {
            card.setStrokeWidth(card == selected ? strokeWidth : 0);
        }
    }
}
