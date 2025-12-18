package com.scentify.app.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.scentify.app.R;
import com.scentify.app.ScentifyApplication;
import com.scentify.app.data.model.CartItem;
import com.scentify.app.databinding.FragmentCartBinding;
import com.scentify.app.viewmodel.CartViewModel;
import com.scentify.app.viewmodel.ScentViewModelFactory;

import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.CartItemActionListener {
    private FragmentCartBinding binding;
    private CartViewModel viewModel;
    private CartAdapter adapter;
    private List<CartItem> currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CartAdapter(this);
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCart.setAdapter(adapter);

        ScentifyApplication app = (ScentifyApplication) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new ScentViewModelFactory(app.getRepository()))
                .get(CartViewModel.class);

        viewModel.getCartItems().observe(getViewLifecycleOwner(), this::renderCart);
        viewModel.getTotalPrice().observe(getViewLifecycleOwner(), total -> {
            double safeTotal = total == null ? 0.0 : total;
            binding.textCartTotal.setText(String.format(Locale.getDefault(), "$%.2f", safeTotal));
        });

        binding.buttonCheckout.setOnClickListener(v -> performCheckout());
        binding.buttonClearCart.setOnClickListener(v -> viewModel.clearCart());
    }

    private void renderCart(List<CartItem> items) {
        currentItems = items;
        adapter.submitList(items);
        boolean isEmpty = items == null || items.isEmpty();
        binding.textEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.layoutCartSummary.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.buttonCheckout.setEnabled(!isEmpty);
        binding.buttonClearCart.setEnabled(!isEmpty);
    }

    private void performCheckout() {
        if (currentItems == null || currentItems.isEmpty()) {
            Toast.makeText(requireContext(), R.string.cart_empty_message, Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.checkout_title)
                .setMessage(R.string.checkout_message)
                .setPositiveButton(R.string.checkout_confirm, (dialog, which) -> {
                    viewModel.clearCart();
                    Toast.makeText(requireContext(), R.string.checkout_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onIncrease(CartItem item) {
        viewModel.increaseQuantity(item);
    }

    @Override
    public void onDecrease(CartItem item) {
        viewModel.decreaseQuantity(item);
    }

    @Override
    public void onRemove(CartItem item) {
        viewModel.removeItem(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
