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
import com.example.yumi.utils.GlideUtil;
import java.util.ArrayList;
import java.util.List;


public class MealsGridAdapter extends BaseAdapter {
    private List<Meal> meals = new ArrayList<>();
    private final OnMealClick onMealClick;

    public MealsGridAdapter(OnMealClick onMealClick) {
        this.onMealClick = onMealClick;
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
        return convertView;
    }

    private static class MealViewHolder {
        private final CardView mealCard;
        private final ImageView mealImage;
        private final TextView mealName;

        MealViewHolder(View itemView) {
            mealCard = itemView.findViewById(R.id.meal_card);
            mealImage = itemView.findViewById(R.id.meal_image);
            mealName = itemView.findViewById(R.id.meal_name);
        }
    }
}
