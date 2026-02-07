package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.presentation.shared.callbacks.OnIngredientClick;
import com.example.yumi.utils.GlideUtil;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class IngredientSearchGridViewAdapter extends BaseAdapter {
    private List<Ingredient> ingredients = new ArrayList<>();
    private final OnIngredientClick onIngredientClick;

    public IngredientSearchGridViewAdapter(OnIngredientClick onIngredientClick) {
        this.onIngredientClick = onIngredientClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Ingredient getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IngredientViewHolder holder;
        Context context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.ingredient_raw_item, parent, false);
            holder = new IngredientViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (IngredientViewHolder) convertView.getTag();
        }

        Ingredient ingredient = getItem(position);

        holder.tvIngredientName.setText(ingredient.getName());
        GlideUtil.getImageWithGeneratedBackground(context,
                holder.ivIngredientImage,
                holder.cardIngredient,
                ingredient.getThumbnailUrl());

        holder.cardIngredient.setOnClickListener(v -> {
            if (onIngredientClick != null) {
                onIngredientClick.onClick(ingredient);
            }
        });

        return convertView;
    }

    private static class IngredientViewHolder {
        private final MaterialCardView cardIngredient;
        private final ImageView ivIngredientImage;
        private final TextView tvIngredientName;

        IngredientViewHolder(View itemView) {
            cardIngredient = itemView.findViewById(R.id.ingredient_background);
            ivIngredientImage = itemView.findViewById(R.id.ingredient_image_view);
            tvIngredientName = itemView.findViewById(R.id.ingredient_text_view);
        }
    }
}