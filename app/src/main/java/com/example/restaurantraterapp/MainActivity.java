package com.example.restaurantraterapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.restaurantraterapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // using viewbinding object to access layout views.
    private ActivityMainBinding binding;
    // Creates instances of fragments once to reuse them and preserve their state across switches
    private final Fragment restaurantFragment = new RestaurantFragment();
    private final Fragment dishRatingFragment = new RatingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflating the layout and setting it as the content view.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // This will set the initial fragment only if it's the first time running
        if (savedInstanceState == null) {
            loadFragment(restaurantFragment);
        }

        // Bottom navigation listener to switch between fragments.
        setupBottomNavigationView();
    }

    /**
     * This function initializes the BottomNavigationView and handles navigation when items are clicked.
     * It loads the corresponding fragment when a menu item is selected.
     */
    private void setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                // Determine which fragment to load based on the Bottom Navigation item ID
                if (itemId == R.id.nav_restaurant) {
                    selectedFragment = restaurantFragment;
                } else if (itemId == R.id.nav_rate) {
                    selectedFragment = dishRatingFragment;
                }

                // Load the selected fragment into the fragment container.
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * This function replaces the current fragment inside the fragment container
     * with the one passed as a parameter
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the fragment container with the new fragment
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);

        // Commit the transaction to apply the change
        fragmentTransaction.commit();
    }
}
