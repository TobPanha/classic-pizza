package com.scentify.app.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.data.model.Product;
import com.google.android.material.chip.Chip;
import com.scentify.app.databinding.FragmentSearchBinding;
import com.scentify.app.ui.home.ProductAdapter;
import com.scentify.app.ui.product.ProductDetailActivity;
import com.scentify.app.viewmodel.ScentViewModelFactory;
import com.scentify.app.viewmodel.SearchViewModel;

import java.util.List;

public class SearchFragment extends Fragment implements ProductAdapter.ProductClickListener {
    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductAdapter(this);
        binding.recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerSearch.setAdapter(adapter);

        ScentifyApplication app = (ScentifyApplication) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository()))
                .get(SearchViewModel.class);

        binding.searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setQuery(query);
                binding.searchInput.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setQuery(newText);
                return true;
            }
        });
        String existingQuery = viewModel.getQuery().getValue();
        if (existingQuery != null && !existingQuery.isEmpty()) {
            binding.searchInput.setQuery(existingQuery, false);
        }
        populateFilterChips();
        viewModel.getResults().observe(getViewLifecycleOwner(), this::renderResults);
    }

    private void renderResults(List<Product> products) {
        adapter.submitList(products);
        binding.textEmpty.setVisibility(products == null || products.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void populateFilterChips() {
        binding.chipFilters.removeAllViews();
        String[] filters = getResources().getStringArray(R.array.fragrance_types);
        for (String filter : filters) {
            Chip chip = new Chip(requireContext());
            chip.setText(filter);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextColor(requireContext().getColorStateList(R.color.chip_text_color));
            chip.setEnsureMinTouchTargetSize(false);
            binding.chipFilters.addView(chip);
        }
    }

    @Override
    public void onProductClicked(Product product) {
        Intent intent = ProductDetailActivity.createIntent(requireContext(), product.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
