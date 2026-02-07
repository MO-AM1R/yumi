package com.example.yumi.presentation.browse.adapters;
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
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.presentation.shared.callbacks.OnCategoryClick;
import com.example.yumi.utils.GlideUtil;

import java.util.List;


public class CategoriesBrowserAdapter extends RecyclerView.Adapter<CategoriesBrowserAdapter.AreaViewHolder> {
    private List<Category> categories;
    private final OnCategoryClick onCategoryClick;

    public CategoriesBrowserAdapter(List<Category> categories, OnCategoryClick onCategoryClick) {
        this.categories = categories;
        this.onCategoryClick = onCategoryClick;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.category_browse_raw_item, parent, false);

        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Category category = categories.get(position);
        View view = holder.getItemView();

        holder.getCategoryName().setText(category.getName());

        GlideUtil.getImageWithGeneratedBackground(
                view.getContext(),
                holder.getCategoryImage(),
                holder.getCardBackground(),
                category.getThumbnailUrl()
        );

        holder.getCardView().setOnClickListener(v -> onCategoryClick.onClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        private final ImageView categoryImage;
        private final TextView categoryName;
        private final CardView cardBackground, cardView;
        private final View itemView;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            categoryImage = itemView.findViewById(R.id.category_image_view);
            categoryName = itemView.findViewById(R.id.category_name);
            cardBackground = itemView.findViewById(R.id.category_background);
            cardView = itemView.findViewById(R.id.category_raw);
        }

        public CardView getCardView() {
            return cardView;
        }

        public View getItemView() {
            return itemView;
        }

        public ImageView getCategoryImage() {
            return categoryImage;
        }

        public TextView getCategoryName() {
            return categoryName;
        }

        public CardView getCardBackground() {
            return cardBackground;
        }
    }
}
