package com.example.yumi.presentation.browse.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.callbacks.OnMealClick;
import com.example.yumi.presentation.shared.callbacks.OnAddToPlanButtonClick;
import com.example.yumi.presentation.shared.callbacks.OnFavButtonClick;
import com.example.yumi.utils.GlideUtil;
import java.util.ArrayList;
import java.util.List;
import eightbitlab.com.blurview.BlurView;


public class MealsGridAdapter extends BaseAdapter {
    private List<Meal> meals = new ArrayList<>();
    private final OnMealClick onMealClick;
    private final OnFavButtonClick onFavButtonClick;
    private final OnAddToPlanButtonClick addToPlanButtonClick;

    public MealsGridAdapter(OnMealClick onMealClick, OnFavButtonClick onFavButtonClick, OnAddToPlanButtonClick addToPlanButtonClick) {
        this.onMealClick = onMealClick;
        this.onFavButtonClick = onFavButtonClick;
        this.addToPlanButtonClick = addToPlanButtonClick;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_meal_grid, parent, false);
            holder = new MealViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MealViewHolder) convertView.getTag();
        }

        Meal meal = getItem(position);
        Context context = parent.getContext();

        holder.mealName.setText(meal.getName());
        GlideUtil.getImage(context, holder.mealImage, meal.getThumbnailUrl());

        holder.mealCard.setOnClickListener(v -> onMealClick.onClick(meal));
        holder.favBtn.setOnClickListener(v -> onFavButtonClick.onClick(meal));
        holder.addToPlanBtn.setOnClickListener(v -> addToPlanButtonClick.onClick(meal));

        return convertView;
    }

    private static class MealViewHolder {
        private final CardView mealCard;
        private final ImageView mealImage;
        private final BlurView favBtn;
        private final TextView mealName;
        private final View addToPlanBtn;

        MealViewHolder(View itemView) {
            mealCard = itemView.findViewById(R.id.meal_card);
            mealImage = itemView.findViewById(R.id.meal_image);
            favBtn = itemView.findViewById(R.id.fav_btn);
            mealName = itemView.findViewById(R.id.meal_name);
            addToPlanBtn = itemView.findViewById(R.id.add_to_plan_btn);
        }
    }
}
