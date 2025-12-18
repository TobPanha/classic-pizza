package com.scentify.app.ui.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scentify.app.data.model.CartItem;
import com.scentify.app.databinding.ItemCartProductBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public interface CartItemActionListener {
        void onIncrease(CartItem item);

        void onDecrease(CartItem item);

        void onRemove(CartItem item);
    }

    private final List<CartItem> items = new ArrayList<>();
    private final CartItemActionListener listener;

    public CartAdapter(CartItemActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CartItem> cartItems) {
        items.clear();
        if (cartItems != null) {
            items.addAll(cartItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCartProductBinding binding = ItemCartProductBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartProductBinding binding;

        CartViewHolder(ItemCartProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartItem item) {
            binding.textCartProductName.setText(item.getProduct().getName());
            binding.textCartProductBrand.setText(item.getProduct().getBrand());
            binding.textCartQuantity.setText(String.valueOf(item.getQuantity()));
            binding.textCartPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getProduct().getPrice()));
            binding.textCartLineTotal.setText(String.format(Locale.getDefault(), "$%.2f", item.getLineTotal()));

            binding.buttonIncrease.setOnClickListener(v -> listener.onIncrease(item));
            binding.buttonDecrease.setOnClickListener(v -> listener.onDecrease(item));
            binding.buttonRemove.setOnClickListener(v -> listener.onRemove(item));
        }
    }
}
