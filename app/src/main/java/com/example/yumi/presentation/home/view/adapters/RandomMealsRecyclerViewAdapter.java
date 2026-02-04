package com.example.yumi.presentation.home.view.adapters;
import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.callbacks.OnMealClick;
import com.example.yumi.utils.GlideUtil;

import java.util.List;

public class RandomMealsRecyclerViewAdapter extends RecyclerView.Adapter<RandomMealsRecyclerViewAdapter.RandomMealsViewHolder> {
    private final OnMealClick onMealClick;
    private List<Meal> meals;

    public RandomMealsRecyclerViewAdapter(OnMealClick onMealClick, List<Meal> meals) {
        this.onMealClick = onMealClick;
        this.meals = meals;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
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
        View view = holder.getMealImage().getRootView();

        holder.getMealName().setText(meal.getName());
        holder.getMealCategory().setText(meal.getCategory());
        holder.getMealIngredientsCount().
                setText(view.getContext().
                        getString(R.string.ingredients_count_message, meal.getIngredients().size()));

        GlideUtil.getImage(
                holder.getMealImage().getContext(),
                holder.getMealImage(),
                meal.getThumbnailUrl()
        );

        view.setOnClickListener(v -> onMealClick.onclick(meal));
    }

    public static class RandomMealsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName, mealIngredientsCount,
                mealCategory;
        private final ImageView mealImage;


        public RandomMealsViewHolder(@NonNull View itemView) {
            super(itemView);
            mealIngredientsCount = itemView.findViewById(R.id.random_ingredients_count);
            mealCategory = itemView.findViewById(R.id.random_meal_category);
            mealImage = itemView.findViewById(R.id.random_meal_image);
            mealName = itemView.findViewById(R.id.random_meal_name);
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
    }
}
