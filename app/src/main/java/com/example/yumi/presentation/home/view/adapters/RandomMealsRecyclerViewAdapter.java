package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.callbacks.OnMealClick;
import com.example.yumi.presentation.shared.callbacks.OnAddToPlanButtonClick;
import com.example.yumi.utils.GlideUtil;
import java.util.List;

public class RandomMealsRecyclerViewAdapter extends RecyclerView.Adapter<RandomMealsRecyclerViewAdapter.RandomMealsViewHolder> {
    private final OnAddToPlanButtonClick addToPlanButtonClick;
    private final OnMealClick onMealClick;
    private List<Meal> meals;

    public RandomMealsRecyclerViewAdapter(
            OnMealClick onMealClick, OnAddToPlanButtonClick onAddToPlanButtonClick, List<Meal> meals) {
        this.onMealClick = onMealClick;
        this.addToPlanButtonClick = onAddToPlanButtonClick;
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

        holder.getMealName().setText(meal.getName());
        holder.getMealCategory().setText(meal.getCategory());
        holder.getMealIngredientsCount()
                .setText(holder.itemView.getContext()
                        .getString(R.string.ingredients_count_message, meal.getIngredients().size()));

        GlideUtil.getImage(
                holder.getMealImage().getContext(),
                holder.getMealImage(),
                meal.getThumbnailUrl()
        );

        holder.getAddToPlanButton().setOnClickListener(v -> addToPlanButtonClick.onClick(meal));
        holder.getCardView().setOnClickListener(v -> onMealClick.onClick(meal));
    }

    public static class RandomMealsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName, mealIngredientsCount, mealCategory;
        private final ImageView mealImage;
        private final CardView cardView;
        private final Button addToPlanButton;

        public RandomMealsViewHolder(@NonNull View itemView) {
            super(itemView);
            mealIngredientsCount = itemView.findViewById(R.id.random_ingredients_count);
            mealCategory = itemView.findViewById(R.id.random_meal_category);
            mealImage = itemView.findViewById(R.id.random_meal_image);
            mealName = itemView.findViewById(R.id.random_meal_name);
            cardView = itemView.findViewById(R.id.random_meal_card);
            addToPlanButton = itemView.findViewById(R.id.random_add_to_plan_btn);
        }

        public TextView getMealName() {
            return mealName;
        }

        public Button getAddToPlanButton() {
            return addToPlanButton;
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
