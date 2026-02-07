package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.callbacks.OnMealClick;
import com.example.yumi.presentation.shared.callbacks.OnAddToPlanButtonClick;
import com.example.yumi.utils.GlideUtil;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;


public class MealSearchGridView extends BaseAdapter {
    private List<Meal> meals = new ArrayList<>();
    private final OnMealClick onMealClick;
    private final OnAddToPlanButtonClick onAddClick;


    public MealSearchGridView(OnMealClick onMealClick, OnAddToPlanButtonClick onAddClick) {
        this.onMealClick = onMealClick;
        this.onAddClick = onAddClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return meals.size();
    }

    @Override
    public Meal getItem(int position) {
        return meals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MealViewHolder holder;
        Context context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_meal_search_row, parent, false);
            holder = new MealViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MealViewHolder) convertView.getTag();
        }

        Meal meal = getItem(position);

        holder.tvMealName.setText(meal.getName());

        int ingredientsCount = meal.getIngredients().size();
        holder.tvIngredientsCount.setText(
                holder.tvMealName.getContext().
                        getString(R.string.ingredients_count_message,
                                ingredientsCount)
        );

        GlideUtil.getImage(context, holder.ivMealImage, meal.getThumbnailUrl());

        holder.cardMeal.setOnClickListener(v -> {
            if (onMealClick != null) {
                onMealClick.onClick(meal);
            }
        });

        holder.btnAdd.setOnClickListener(v -> {
            if (onAddClick != null) {
                onAddClick.onClick(meal);
            }
        });

        return convertView;
    }

    private static class MealViewHolder {
        private final MaterialCardView cardMeal;
        private final ImageView ivMealImage;
        private final TextView tvMealName;
        private final TextView tvIngredientsCount;
        private final TextView btnAdd;

        MealViewHolder(View itemView) {
            cardMeal = itemView.findViewById(R.id.card_meal);
            ivMealImage = itemView.findViewById(R.id.iv_meal_image);
            tvMealName = itemView.findViewById(R.id.tv_meal_name);
            tvIngredientsCount = itemView.findViewById(R.id.tv_ingredients_count);
            btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }
}