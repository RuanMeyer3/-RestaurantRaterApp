package com.example.restaurantraterapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.restaurantraterapp.databinding.FragmentRatingBinding;

public class RatingFragment extends Fragment {

    // View binding instance to access views safely
    private FragmentRatingBinding binding;
    // Viewmodel is used to share data between fragments
    private RatingViewModel viewModel;
    // Variable used to store the current selected dish type
    private String selectedDishType = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the view Binding
        binding = FragmentRatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the shared ViewModel scoped to the activity
        viewModel = new ViewModelProvider(requireActivity()).get(RatingViewModel.class);

        // Initialize the spinner and submit button functionality
        setupSpinner();
        setupSubmitButton();
        //Updates UI with submitted results
        observeViewModelResult();
    }

    /**
     * This function configures the spinner that allows the user to select a dish type.
     * It used items from the dish_types_array in strings.xml
     */
    private void setupSpinner() {
        // Use ArrayAdapter to populate the Spinner from the resource array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                // As mentioned, the array is defined in strings.xml
                R.array.dish_types_array,
                android.R.layout.simple_spinner_item
        );

        //Specifying the layout fro the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDishType.setAdapter(adapter);

        // Listener to update selectedDishType when the user chooses an item
        binding.spinnerDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // Updating selectedDishType whenever a new item is selected
                selectedDishType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ensure a default selection is maintained
                selectedDishType = getResources().getStringArray(R.array.dish_types_array)[0];
            }
        });

        // Ensure the initial value is set, in case onItemSelected is not called immediately
        selectedDishType = getResources().getStringArray(R.array.dish_types_array)[0];
    }


    /**
     * This function sets up the submit button functionality.
     * When pressed, it validates input and submits the rating to the viewmodel
     */
    private void setupSubmitButton() {
        binding.btnSubmitRating.setOnClickListener(v -> {
            // Retrieving the entered dish name and rating value
            String dishName = binding.etDishName.getText().toString().trim();
            float rating = binding.ratingBar.getRating(); // RatingBar captures rating from 1.0 to 5.0

            // Validation to ensure dish name is not empty
            if (dishName.isEmpty()) {
                binding.etDishName.setError("Dish name is required");
                return;
            }

            // Submit the rating to the shared ViewModel
            viewModel.submitRating(dishName, selectedDishType, rating);

            // Display the entered information as a Toast (Requirement 3)
            String toastMessage = String.format("Rating Submitted!\nDish: %s | Rating: %.1f stars",
                    dishName, rating);
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show();

            // Clear inputs for next entry
            binding.etDishName.setText("");
            binding.ratingBar.setRating(3.0f); // Reset rating to a default of 3
        });
    }

    /**
     * Observes LiveData in the ViewModel to update the display TextView.
     * Displays the most recently submitted rating, or a placeholder message if none exist.
     */
    private void observeViewModelResult() {
        viewModel.getLatestRatingResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null && !result.isEmpty()) {
                binding.tvResultDisplay.setText(result);
            } else {
                binding.tvResultDisplay.setText("No ratings submitted yet.");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clearing binding reference
        binding = null;
    }
}