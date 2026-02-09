package com.example.yumi.presentation.home.view.adapters;
import static com.example.yumi.presentation.home.view.adapters.CategoriesRecyclerViewAdapter.*;
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
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.presentation.shared.callbacks.OnCategoryClick;
import com.example.yumi.utils.GlideUtil;
import java.util.List;


public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Category> categories;
    private final OnCategoryClick onCategoryClick;

    public CategoriesRecyclerViewAdapter(List<Category> categories, OnCategoryClick onCategoryClick) {
        this.categories = categories;
        this.onCategoryClick = onCategoryClick;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.category_raw_item, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        View view = holder.getItemView();

        int paddingEnd = 0;
        if (position < categories.size() - 1){
            paddingEnd = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16,
                    view.getResources().getDisplayMetrics()
            );
        }

        view.setPaddingRelative(0, 0, paddingEnd, 0);

        holder.getCategoryTxtView().setText(category.getName());
        GlideUtil.getImageWithGeneratedBackground(
                view.getContext(),
                holder.getCategoryImageView(),
                holder.getCardBackground(),
                category.getThumbnailUrl()
        );

        view.setOnClickListener(v -> onCategoryClick.onClick(category));
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

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTxtView;
        private final ImageView categoryImageView;
        private final CardView cardBackground;
        private final View itemView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            categoryTxtView = itemView.findViewById(R.id.category_text_view);
            categoryImageView = itemView.findViewById(R.id.category_image_view);
            cardBackground = itemView.findViewById(R.id.category_background);
        }

        public View getItemView() {
            return itemView;
        }

        public TextView getCategoryTxtView() {
            return categoryTxtView;
        }

        public ImageView getCategoryImageView() {
            return categoryImageView;
        }

        public CardView getCardBackground() {
            return cardBackground;
        }
    }
}
