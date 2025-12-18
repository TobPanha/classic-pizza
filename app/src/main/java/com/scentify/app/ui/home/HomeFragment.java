package com.scentify.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.data.local.SessionManager;
import com.scentify.app.data.model.Product;
import com.scentify.app.databinding.FragmentHomeBinding;
import com.scentify.app.ui.MainActivity;
import com.scentify.app.ui.product.ProductDetailActivity;
import com.scentify.app.ui.quiz.QuizActivity;
import com.scentify.app.viewmodel.HomeViewModel;
import com.scentify.app.viewmodel.ScentViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.ProductClickListener, HighlightAdapter.HighlightListener {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private ProductAdapter adapter;
    private HighlightAdapter highlightAdapter;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        adapter = new ProductAdapter(this);
        highlightAdapter = new HighlightAdapter(this);
        binding.recyclerRecommended.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerRecommended.setAdapter(adapter);
        binding.recyclerHot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerHot.setAdapter(highlightAdapter);
        String email = sessionManager.getEmail();
        binding.textUserEmail.setText(email.isEmpty() ? getString(R.string.app_name) : email);
        binding.buttonExplore.setOnClickListener(v -> binding.scrollHome.post(() ->
                binding.scrollHome.smoothScrollTo(0, binding.recyclerRecommended.getTop())));
        binding.buttonTakeQuiz.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), QuizActivity.class)));
        binding.buttonSettingsShortcut.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).openSettingsFromShortcut();
            }
        });
        populateBrandChips();
        ScentifyApplication app = (ScentifyApplication) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository()))
                .get(HomeViewModel.class);
        viewModel.getRankedProducts().observe(getViewLifecycleOwner(), this::renderProducts);
    }

    private void renderProducts(List<Product> products) {
        adapter.submitList(products);
        if (products == null || products.isEmpty()) {
            binding.textEmpty.setVisibility(View.VISIBLE);
            binding.textEmpty.setText(R.string.home_empty_state);
            highlightAdapter.submit(new ArrayList<>());
        } else {
            binding.textEmpty.setVisibility(View.GONE);
            int limit = Math.min(5, products.size());
            highlightAdapter.submit(new ArrayList<>(products.subList(0, limit)));
        }
    }

    @Override
    public void onProductClicked(Product product) {
        openProduct(product);
    }

    @Override
    public void onHighlightClicked(Product product) {
        openProduct(product);
    }

    private void openProduct(Product product) {
        Intent intent = ProductDetailActivity.createIntent(requireContext(), product.getId());
        startActivity(intent);
    }

    private void populateBrandChips() {
        binding.chipTopBrands.removeAllViews();
        String[] brands = getResources().getStringArray(R.array.brand_categories);
        for (String brand : brands) {
            Chip chip = new Chip(requireContext());
            chip.setText(brand);
            chip.setClickable(false);
            chip.setCheckable(false);
            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextColor(requireContext().getColorStateList(R.color.chip_text_color));
            chip.setEnsureMinTouchTargetSize(false);
            binding.chipTopBrands.addView(chip);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
