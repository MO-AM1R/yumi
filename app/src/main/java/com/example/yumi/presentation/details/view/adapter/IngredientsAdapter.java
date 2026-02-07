package com.example.yumi.presentation.details.view.adapter;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.MealIngredient;
import java.util.ArrayList;
import java.util.List;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<MealIngredient> ingredients = new ArrayList<>();

    public IngredientsAdapter(){

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setIngredients(List<MealIngredient> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_details, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ingredientImage;
        private final TextView ingredientName;
        private final TextView measure;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredient_image);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            measure = itemView.findViewById(R.id.ingredient_measure);
        }

        public void bind(MealIngredient ingredient) {
            ingredientName.setText(ingredient.getName());
            measure.setText(ingredient.getMeasure());

            Glide.with(ingredientImage.getContext())
                    .load(ingredient.getThumbnailUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ingredientImage);
        }
    }
}