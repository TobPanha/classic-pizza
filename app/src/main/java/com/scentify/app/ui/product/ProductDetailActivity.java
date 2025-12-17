package com.scentify.app.ui.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.data.model.Product;
import com.scentify.app.databinding.ActivityProductDetailBinding;
import com.scentify.app.viewmodel.ProductDetailViewModel;
import com.scentify.app.viewmodel.ScentViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private ActivityProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private int desiredQuantity = 1;

    public static Intent createIntent(Context context, String productId) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        if (productId == null) {
            finish();
            return;
        }
        ScentifyApplication app = (ScentifyApplication) getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository(), productId))
                .get(ProductDetailViewModel.class);

        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.buttonIncrease.setOnClickListener(v -> updateQuantity(desiredQuantity + 1));
        binding.buttonDecrease.setOnClickListener(v -> {
            if (desiredQuantity > 1) {
                updateQuantity(desiredQuantity - 1);
            }
        });
        binding.buttonAddToCart.setOnClickListener(v -> {
            Product product = viewModel.getProduct().getValue();
            if (product != null) {
                viewModel.addToCart(product, desiredQuantity);
                Toast.makeText(this, R.string.cart_added_message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getProduct().observe(this, this::renderProduct);
    }

    private void renderProduct(Product product) {
        if (product == null) {
            binding.textProductName.setText(R.string.product_not_found);
            return;
        }
        binding.imageHero.setImageResource(R.drawable.ic_perfume);
        binding.textProductName.setText(product.getName());
        binding.textProductBrand.setText(product.getBrand());
        binding.textProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
        renderTags(product.getTags());
        binding.textProductDescription.setText(R.string.product_detail_description);
    }

    private void updateQuantity(int quantity) {
        desiredQuantity = quantity;
        binding.textQuantity.setText(String.valueOf(quantity));
    }

    private void renderTags(List<String> tags) {
        binding.chipTags.removeAllViews();
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
            formatted.add(getString(R.string.product_tags_default));
        }
        for (String label : formatted) {
            binding.chipTags.addView(buildTagChip(label));
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

    private Chip buildTagChip(String label) {
        Chip chip = new Chip(this);
        chip.setText(label);
        chip.setCheckable(true);
        chip.setChecked(true);
        chip.setClickable(false);
        chip.setEnsureMinTouchTargetSize(false);
        chip.setTextAppearance(R.style.TextAppearance_Scentify_Caption);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.chip_text_color));
        chip.setChipBackgroundColorResource(R.color.chip_background_color);
        chip.setRippleColorResource(R.color.ripple_surface);
        chip.setCheckedIconVisible(false);
        chip.setChipStrokeWidth(0f);
        chip.setChipCornerRadius(getResources().getDimension(R.dimen.scentify_chip_radius));
        return chip;
    }
}
