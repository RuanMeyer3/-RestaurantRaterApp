package com.example.restaurantraterapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.restaurantraterapp.databinding.FragmentRestaurantBinding;

import java.util.List;

public class RestaurantFragment extends Fragment {

    private FragmentRestaurantBinding binding;
    private RatingViewModel viewModel;
    private RatingsAdapter adapter;

    // Hardcoded restaurant data as required
    private static final String RESTAURANT_NAME = "The Local Bistro";
    private static final String RESTAURANT_ADDRESS = "123 Main Street, Anytown";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the shared ViewModel scoped to the Activity
        viewModel = new ViewModelProvider(requireActivity()).get(RatingViewModel.class);

        // Display static restaurant info
        binding.tvRestaurantName.setText(RESTAURANT_NAME);
        binding.tvRestaurantAddress.setText(RESTAURANT_ADDRESS);

        // Setup RecyclerView
        setupRecyclerView();

        // Observe the ViewModel for all ratings
        observeRatingsList();
    }

    private void setupRecyclerView() {
        // Initialize adapter
        adapter = new RatingsAdapter();

        // Set up RecyclerView
        binding.rvRatings.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRatings.setAdapter(adapter);

        // Optional: Add item click listeners (e.g., for deleting ratings)
        adapter.setOnRatingClickListener(new RatingsAdapter.OnRatingClickListener() {
            @Override
            public void onRatingClick(RatingViewModel.DishRating rating, int position) {
                // Handle item click if needed
                // For example, show details in a dialog
            }

            @Override
            public void onDeleteClick(RatingViewModel.DishRating rating, int position) {
                // Delete the rating
                viewModel.removeRating(position);
            }
        });
    }

    private void observeRatingsList() {
        // Observe the list of all ratings
        viewModel.getRatingsList().observe(getViewLifecycleOwner(), ratings -> {
            if (ratings != null && !ratings.isEmpty()) {
                // Show RecyclerView, hide empty state
                binding.rvRatings.setVisibility(View.VISIBLE);
                binding.tvEmptyState.setVisibility(View.GONE);
                binding.summarySection.setVisibility(View.VISIBLE);

                // Submit list to adapter
                adapter.submitList(ratings);

                // Update summary information
                binding.tvTotalRatings.setText("Total: " + ratings.size() + " ratings");
                binding.tvAverageRating.setText(String.format("Avg: %.1f ★",
                        viewModel.getAverageRating()));
            } else {
                // Show empty state, hide RecyclerView
                binding.rvRatings.setVisibility(View.GONE);
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.summarySection.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up binding reference
    }
}