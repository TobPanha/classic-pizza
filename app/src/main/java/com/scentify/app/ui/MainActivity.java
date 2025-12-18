package com.scentify.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.scentify.app.R;
import com.scentify.app.databinding.ActivityMainBinding;
import com.scentify.app.ui.cart.CartFragment;
import com.scentify.app.ui.home.HomeFragment;
import com.scentify.app.ui.search.SearchFragment;
import com.scentify.app.ui.settings.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();
    private int currentMenuId = R.id.menu_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            binding.bottomNavigation.setSelectedItemId(R.id.menu_home);
            switchFragment(R.id.menu_home);
        } else {
            currentMenuId = savedInstanceState.getInt("currentMenuId", R.id.menu_home);
            switchFragment(currentMenuId);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentMenuId", currentMenuId);
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        if (item.getItemId() != currentMenuId) {
            switchFragment(item.getItemId());
        }
        return true;
    };

    private void switchFragment(int menuId) {
        Fragment fragment = fragmentMap.get(menuId);
        if (fragment == null) {
            fragment = createFragment(menuId);
            fragmentMap.put(menuId, fragment);
        }
        currentMenuId = menuId;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private Fragment createFragment(int menuId) {
        if (menuId == R.id.menu_search) {
            return new SearchFragment();
        } else if (menuId == R.id.menu_saved) {
            return new CartFragment();
        } else if (menuId == R.id.menu_settings) {
            return new SettingsFragment();
        }
        return new HomeFragment();
    }

    public void openSettingsFromShortcut() {
        if (binding != null) {
            binding.bottomNavigation.setSelectedItemId(R.id.menu_settings);
        }
    }
}
