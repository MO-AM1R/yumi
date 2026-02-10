package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.callbacks.IsFavorite;
import com.example.yumi.presentation.home.view.callbacks.OnMealClick;
import com.example.yumi.presentation.shared.callbacks.OnFavButtonClick;
import com.example.yumi.utils.GlideUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eightbitlab.com.blurview.BlurView;

public class RandomMealsRecyclerViewAdapter extends RecyclerView.Adapter<RandomMealsRecyclerViewAdapter.RandomMealsViewHolder> {
    private final OnMealClick onMealClick;
    private final OnFavButtonClick onFavButtonClick;
    private final IsFavorite isFavorite;
    private List<Meal> meals;
    private Set<String> favoriteIds;

    public RandomMealsRecyclerViewAdapter(
            OnMealClick onMealClick, OnFavButtonClick onFavButtonClick,
            IsFavorite isFavorite, List<Meal> meals) {
        this.onFavButtonClick = onFavButtonClick;
        this.onMealClick = onMealClick;
        this.meals = meals;
        this.isFavorite = isFavorite;
        this.favoriteIds = new HashSet<>();
    }

    public void updateFavoriteIcon(int position, boolean isFavorite) {
        if (position >= 0 && position < meals.size()) {
            String mealId = meals.get(position).getId();
            if (isFavorite) {
                favoriteIds.add(mealId);
            } else {
                favoriteIds.remove(mealId);
            }
        }
        notifyItemChanged(position, isFavorite);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMeals(List<Meal> meals, Set<String> favoriteIds) {
        this.meals = meals;
        this.favoriteIds = favoriteIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RandomMealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.random_meal_item_raw, parent, false);
        return new RandomMealsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RandomMealsViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.getMealName().setText(meal.getName());
        holder.getMealCategory().setText(meal.getCategory());
        holder.getMealIngredientsCount()
                .setText(holder.itemView.getContext()
                        .getString(R.string.ingredients_count_message, meal.getIngredients().size()));

        boolean isFav = favoriteIds.contains(meal.getId());
        holder.getFavIcon().setImageResource(
                isFav ? R.drawable.favorite_filled : R.drawable.favorite
        );

        GlideUtil.getImage(
                holder.getMealImage().getContext(),
                holder.getMealImage(),
                meal.getThumbnailUrl()
        );

        holder.getCardView().setOnClickListener(v -> onMealClick.onClick(meal));

        holder.getFavoriteButton().setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onFavButtonClick.onClick(meal, adapterPosition);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RandomMealsViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            for (Object payload : payloads) {
                if (payload instanceof Boolean) {
                    boolean isFavorite = (Boolean) payload;
                    holder.getFavIcon().setImageResource(
                            isFavorite ? R.drawable.favorite_filled : R.drawable.favorite
                    );
                }
            }
        }
    }

    public static class RandomMealsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName, mealIngredientsCount, mealCategory;
        private final ImageView mealImage;
        private final ImageView favIcon;
        private final CardView cardView;
        private final BlurView favoriteButton;

        public RandomMealsViewHolder(@NonNull View itemView) {
            super(itemView);
            mealIngredientsCount = itemView.findViewById(R.id.random_ingredients_count);
            mealCategory = itemView.findViewById(R.id.random_meal_category);
            mealImage = itemView.findViewById(R.id.random_meal_image);
            mealName = itemView.findViewById(R.id.random_meal_name);
            cardView = itemView.findViewById(R.id.random_meal_card);
            favoriteButton = itemView.findViewById(R.id.random_fav_btn);
            favIcon = itemView.findViewById(R.id.fav_icon);
        }

        public ImageView getFavIcon() {
            return favIcon;
        }

        public BlurView getFavoriteButton() {
            return favoriteButton;
        }

        public TextView getMealName() {
            return mealName;
        }

        public TextView getMealIngredientsCount() {
            return mealIngredientsCount;
        }

        public TextView getMealCategory() {
            return mealCategory;
        }

        public ImageView getMealImage() {
            return mealImage;
        }

        public CardView getCardView() {
            return cardView;
        }
    }
}
