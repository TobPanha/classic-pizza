package com.scentify.app.ui.quiz;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.scentify.app.R;
import com.scentify.app.databinding.ItemQuizMatchBinding;
import com.scentify.app.quiz.QuizProductMatch;
import com.scentify.app.utils.TagFormatter;

import java.util.ArrayList;
import java.util.List;

class QuizMatchAdapter extends RecyclerView.Adapter<QuizMatchAdapter.MatchViewHolder> {
    private final List<QuizProductMatch> matches = new ArrayList<>();

    void submit(List<QuizProductMatch> newMatches) {
        matches.clear();
        if (newMatches != null) {
            matches.addAll(newMatches);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemQuizMatchBinding binding = ItemQuizMatchBinding.inflate(inflater, parent, false);
        return new MatchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.bind(matches.get(position));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuizMatchBinding binding;

        MatchViewHolder(ItemQuizMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(QuizProductMatch match) {
            binding.imageProduct.setImageResource(R.drawable.ic_perfume);
            binding.textProductName.setText(match.getProduct().getName());
            binding.textProductBrand.setText(match.getProduct().getBrand());
            binding.textMatchPercent.setText(binding.getRoot().getContext()
                    .getString(R.string.quiz_card_match_percent, match.getMatchPercentage()));
            binding.chipMatchTags.removeAllViews();
            List<String> labels = extractLabels(match.getMatchingTags());
            if (labels.isEmpty()) {
                binding.chipMatchTags.addView(createChip(binding.getRoot().getResources().getString(R.string.product_tags_default)));
                binding.textMatchExplanation.setText(R.string.quiz_result_match_explain);
            } else {
                for (String label : labels) {
                    binding.chipMatchTags.addView(createChip(label));
                }
                String joined = TextUtils.join(", ", labels);
                binding.textMatchExplanation.setText(binding.getRoot().getContext()
                        .getString(R.string.quiz_match_reason, joined));
            }
        }

        private Chip createChip(String label) {
            Chip chip = new Chip(binding.getRoot().getContext());
            chip.setText(label);
            chip.setCheckable(true);
            chip.setChecked(true);
            chip.setEnsureMinTouchTargetSize(false);
            chip.setClickable(false);
            chip.setTextAppearance(R.style.TextAppearance_Scentify_Caption);
            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextColor(binding.getRoot().getContext().getColorStateList(R.color.chip_text_color));
            chip.setChipCornerRadius(binding.getRoot().getResources().getDimension(R.dimen.scentify_chip_radius));
            chip.setChipStrokeWidth(0f);
            chip.setCheckedIconVisible(false);
            return chip;
        }

        private List<String> extractLabels(List<String> rawTags) {
            List<String> labels = new ArrayList<>();
            if (rawTags == null) {
                return labels;
            }
            for (String tag : rawTags) {
                String label = TagFormatter.format(tag);
                if (!label.isEmpty()) {
                    labels.add(label);
                }
            }
            return labels;
        }
    }
}
