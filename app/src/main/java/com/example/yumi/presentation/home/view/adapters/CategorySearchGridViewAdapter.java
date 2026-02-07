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
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.presentation.shared.callbacks.OnCategoryClick;
import com.example.yumi.utils.GlideUtil;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class CategorySearchGridViewAdapter extends BaseAdapter {
    private List<Category> categories = new ArrayList<>();
    private final OnCategoryClick onCategoryClick;


    public CategorySearchGridViewAdapter(OnCategoryClick onCategoryClick) {
        this.onCategoryClick = onCategoryClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryViewHolder holder;
        Context context = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.category_raw_item, parent, false);
            holder = new CategoryViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CategoryViewHolder) convertView.getTag();
        }

        Category category = getItem(position);

        holder.tvCategoryName.setText(category.getName());
        GlideUtil.getImageWithGeneratedBackground(context, holder.ivCategoryImage, holder.cardCategory, category.getThumbnailUrl());

        holder.cardCategory.setOnClickListener(v -> {
            if (onCategoryClick != null) {
                onCategoryClick.onClick(category);
            }
        });

        return convertView;
    }

    private static class CategoryViewHolder {
        private final MaterialCardView cardCategory;
        private final ImageView ivCategoryImage;
        private final TextView tvCategoryName;

        CategoryViewHolder(View itemView) {
            cardCategory = itemView.findViewById(R.id.category_background);
            ivCategoryImage = itemView.findViewById(R.id.category_image_view);
            tvCategoryName = itemView.findViewById(R.id.category_text_view);
        }
    }
}