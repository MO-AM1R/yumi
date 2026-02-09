package com.example.yumi.presentation.home.view.adapters;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.presentation.shared.callbacks.OnIngredientClick;
import com.example.yumi.utils.GlideUtil;

import java.util.List;


public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;
    private final OnIngredientClick onIngredientClick;

    public IngredientsRecyclerViewAdapter(List<Ingredient> ingredients, OnIngredientClick onIngredientClick) {
        this.ingredients = ingredients;
        this.onIngredientClick = onIngredientClick;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.ingredient_raw_item, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        View view = holder.getItemView();

        int paddingEnd = 0;
        if (position < ingredients.size() - 1){
            paddingEnd = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16,
                    view.getResources().getDisplayMetrics()
            );
        }

        view.setPaddingRelative(0, 0, paddingEnd, 0);

        holder.getIngredientName().setText(ingredient.getName());
        GlideUtil.getImageWithGeneratedBackground(
                view.getContext(),
                holder.getIngredientImage(),
                holder.getCardBackground(),
                ingredient.getThumbnailUrl()
        );


        view.setOnClickListener(v -> onIngredientClick.onClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientName;
        private final ImageView ingredientImage;
        private final CardView cardBackground;
        private final View itemView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ingredientName = itemView.findViewById(R.id.ingredient_text_view);
            ingredientImage = itemView.findViewById(R.id.ingredient_image_view);
            cardBackground = itemView.findViewById(R.id.ingredient_background);
        }

        public View getItemView() {
            return itemView;
        }

        public TextView getIngredientName() {
            return ingredientName;
        }

        public ImageView getIngredientImage() {
            return ingredientImage;
        }

        public CardView getCardBackground() {
            return cardBackground;
        }
    }
}
