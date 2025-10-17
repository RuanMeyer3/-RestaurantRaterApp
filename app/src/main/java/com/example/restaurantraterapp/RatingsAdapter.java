package com.example.restaurantraterapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantraterapp.databinding.ItemRatingBinding;

/**
 * Adapter for displaying dish ratings in a RecyclerView.
 */
public class RatingsAdapter extends ListAdapter<RatingViewModel.DishRating, RatingsAdapter.RatingViewHolder> {

    // An optional interface for handling item clicks
    public interface OnRatingClickListener {
        void onRatingClick(RatingViewModel.DishRating rating, int position);
        void onDeleteClick(RatingViewModel.DishRating rating, int position);
    }

    // Listener variable to handle click actions from outside the adapter
    private OnRatingClickListener listener;

    // Constructor to pass the DiffUtil callback
    public RatingsAdapter() {
        super(DIFF_CALLBACK);
    }

    // Setter to assign a click listener from the fragment or activity
    public void setOnRatingClickListener(OnRatingClickListener listener) {
        this.listener = listener;
    }

    // DiffUtil callback for efficient list updates
    private static final DiffUtil.ItemCallback<RatingViewModel.DishRating> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RatingViewModel.DishRating>() {
                @Override
                public boolean areItemsTheSame(@NonNull RatingViewModel.DishRating oldItem,
                                               @NonNull RatingViewModel.DishRating newItem) {
                    // Compare by timestamp since each rating is unique
                    return oldItem.getTimestamp() == newItem.getTimestamp();
                }

                @Override
                public boolean areContentsTheSame(@NonNull RatingViewModel.DishRating oldItem,
                                                  @NonNull RatingViewModel.DishRating newItem) {
                    return oldItem.getDishName().equals(newItem.getDishName()) &&
                            oldItem.getDishType().equals(newItem.getDishType()) &&
                            oldItem.getRating() == newItem.getRating();
                }
            };

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using the generated binding class
        ItemRatingBinding binding = ItemRatingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RatingViewHolder(binding);
    }

    // Called to display the data at a specific position in the list.
    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        RatingViewModel.DishRating rating = getItem(position);
        holder.bind(rating, position);
    }

    // Holds references to item views.
    class RatingViewHolder extends RecyclerView.ViewHolder {
        private final ItemRatingBinding binding;

        public RatingViewHolder(@NonNull ItemRatingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RatingViewModel.DishRating rating, int position) {
            // Display dish information
            binding.tvDishName.setText(rating.getDishName());
            binding.tvDishType.setText(rating.getDishType());
            binding.tvRating.setText(String.format("%.1f stars", rating.getRating()));
            binding.ratingBar.setRating(rating.getRating());

            // Set click listeners
            if (listener != null) {
                binding.getRoot().setOnClickListener(v ->
                        listener.onRatingClick(rating, getAdapterPosition()));

                binding.btnDelete.setOnClickListener(v ->
                        listener.onDeleteClick(rating, getAdapterPosition()));
            }
        }
    }
}