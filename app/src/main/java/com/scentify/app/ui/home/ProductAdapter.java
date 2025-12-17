package com.scentify.app.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.scentify.app.R;
import com.scentify.app.data.model.Product;
import com.scentify.app.databinding.ItemProductCardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public interface ProductClickListener {
        void onProductClicked(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private final ProductClickListener listener;

    public ProductAdapter(ProductClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Product> newItems) {
        products.clear();
        if (newItems != null) {
            products.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProductCardBinding binding = ItemProductCardBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductCardBinding binding;

        ProductViewHolder(ItemProductCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            binding.imageProduct.setImageResource(R.drawable.ic_perfume);
            binding.textProductName.setText(product.getName());
            binding.textProductBrand.setText(product.getBrand());
            binding.textProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
            renderTags(product.getTags());
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClicked(product);
                }
            });
        }

        private void renderTags(List<String> tags) {
            binding.chipGroupTags.removeAllViews();
            List<String> formatted = new ArrayList<>();
            if (tags != null) {
                for (String tag : tags) {
                    String cleaned = formatTag(tag);
                    if (!cleaned.isEmpty()) {
                        formatted.add(cleaned);
                    }
                }
            }
            if (formatted.isEmpty()) {
                formatted.add(binding.getRoot().getResources().getString(R.string.product_tags_default));
            }
            for (String label : formatted) {
                binding.chipGroupTags.addView(createTagChip(label));
            }
        }

        private String formatTag(String tag) {
            if (tag == null) {
                return "";
            }
            String cleaned = tag;
            String[] prefixes = new String[]{"fragrance_", "occasion_", "brand_", "budget_"};
            for (String prefix : prefixes) {
                if (cleaned.startsWith(prefix)) {
                    cleaned = cleaned.substring(prefix.length());
                    break;
                }
            }
            cleaned = cleaned.replace("_", " ").trim();
            if (cleaned.isEmpty()) {
                return "";
            }
            return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1);
        }

        private Chip createTagChip(String label) {
            Chip chip = new Chip(binding.getRoot().getContext());
            chip.setText(label);
            chip.setCheckable(true);
            chip.setChecked(true);
            chip.setClickable(false);
            chip.setEnsureMinTouchTargetSize(false);
            chip.setTextAppearance(R.style.TextAppearance_Scentify_Caption);
            chip.setTextColor(ContextCompat.getColorStateList(chip.getContext(), R.color.chip_text_color));
            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setRippleColorResource(R.color.ripple_surface);
            chip.setElevation(0f);
            chip.setChipStrokeWidth(0f);
            chip.setCheckedIconVisible(false);
            chip.setChipCornerRadius(binding.getRoot().getResources().getDimension(R.dimen.scentify_chip_radius));
            return chip;
        }
    }
}
