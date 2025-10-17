package com.example.restaurantraterapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared ViewModel scoped to the MainActivity to hold and communicate rating data
 * between RestaurantFragment and DishRatingFragment using LiveData.
 */
public class RatingViewModel extends ViewModel {

    // Inner class to represent a single dish rating
    public static class DishRating {
        private String dishName;
        private String dishType;
        private float rating;
        private long timestamp; // To track when the rating was made

        public DishRating(String dishName, String dishType, float rating) {
            this.dishName = dishName;
            this.dishType = dishType;
            this.rating = rating;
            this.timestamp = System.currentTimeMillis();
        }

        public String getDishName() {
            return dishName;
        }

        public String getDishType() {
            return dishType;
        }

        public float getRating() {
            return rating;
        }

        public long getTimestamp() {
            return timestamp;
        }

        /**
         * Returns a formatted string representation of this rating.
         */
        public String getFormattedRating() {
            return "Dish: " + dishName + "\n" +
                    "Type: " + dishType + "\n" +
                    "Rating: " + String.format("%.1f", rating) + " stars";
        }
    }

    // MutableLiveData to hold the list of all ratings
    private final MutableLiveData<List<DishRating>> ratingsList = new MutableLiveData<>(new ArrayList<>());

    // Optional: LiveData for the most recent rating (for backward compatibility)
    private final MutableLiveData<String> latestRatingResult = new MutableLiveData<>();

    /**
     * Exposes the LiveData list for Fragments to observe.
     * @return LiveData object holding the list of all dish ratings.
     */
    public LiveData<List<DishRating>> getRatingsList() {
        return ratingsList;
    }

    /**
     * Exposes the latest rating result as a formatted string.
     * @return LiveData object holding the most recent rating result string.
     */
    public LiveData<String> getLatestRatingResult() {
        return latestRatingResult;
    }

    /**
     * Submits a new rating, adds it to the list, and updates the LiveData.
     * This update is immediately reflected in any observing fragment.
     * @param dishName The name of the dish.
     * @param dishType The selected type of the dish (e.g., Entrée).
     * @param rating The star rating (1.0 to 5.0).
     */
    public void submitRating(String dishName, String dishType, float rating) {
        // Create a new DishRating object
        DishRating newRating = new DishRating(dishName, dishType, rating);

        // Get the current list (create a new list to trigger LiveData observers)
        List<DishRating> currentList = ratingsList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        } else {
            // Create a new list to ensure LiveData detects the change
            currentList = new ArrayList<>(currentList);
        }

        // Add the new rating to the list
        currentList.add(newRating);

        // Update the LiveData with the new list
        ratingsList.setValue(currentList);

        // Also update the latest rating result for backward compatibility
        String resultText = "Successfully rated!\n" + newRating.getFormattedRating();
        latestRatingResult.setValue(resultText);
    }

    /**
     * Gets the total number of ratings.
     * @return The count of all ratings submitted.
     */
    public int getRatingsCount() {
        List<DishRating> list = ratingsList.getValue();
        return list != null ? list.size() : 0;
    }

    /**
     * Calculates the average rating across all dishes.
     * @return The average rating, or 0.0 if no ratings exist.
     */
    public float getAverageRating() {
        List<DishRating> list = ratingsList.getValue();
        if (list == null || list.isEmpty()) {
            return 0.0f;
        }

        float sum = 0;
        for (DishRating rating : list) {
            sum += rating.getRating();
        }
        return sum / list.size();
    }

    /**
     * Clears all ratings from the list.
     */
    public void clearAllRatings() {
        ratingsList.setValue(new ArrayList<>());
        latestRatingResult.setValue("");
    }

    /**
     * Removes a specific rating from the list by index.
     * @param position The position of the rating to remove.
     */
    public void removeRating(int position) {
        List<DishRating> currentList = ratingsList.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList = new ArrayList<>(currentList);
            currentList.remove(position);
            ratingsList.setValue(currentList);
        }
    }
}