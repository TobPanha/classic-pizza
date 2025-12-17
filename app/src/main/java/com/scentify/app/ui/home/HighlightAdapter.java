package com.scentify.app.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scentify.app.data.model.Product;
import com.scentify.app.databinding.ItemHighlightCardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.HighlightViewHolder> {
    interface HighlightListener {
        void onHighlightClicked(Product product);
    }

    private final List<Product> items = new ArrayList<>();
    private final HighlightListener listener;

    HighlightAdapter(HighlightListener listener) {
        this.listener = listener;
    }

    void submit(List<Product> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHighlightCardBinding binding = ItemHighlightCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HighlightViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HighlightViewHolder extends RecyclerView.ViewHolder {
        private final ItemHighlightCardBinding binding;

        HighlightViewHolder(ItemHighlightCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            binding.imageHighlight.setImageResource(com.scentify.app.R.drawable.ic_perfume);
            binding.textHighlightName.setText(product.getName());
            binding.textHighlightBrand.setText(product.getBrand());
            binding.textHighlightPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHighlightClicked(product);
                }
            });
        }
    }
}
