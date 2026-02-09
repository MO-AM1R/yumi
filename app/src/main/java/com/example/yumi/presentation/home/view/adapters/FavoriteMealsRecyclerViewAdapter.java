package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.callbacks.OnDeleteFavClick;
import com.example.yumi.presentation.home.view.callbacks.OnMealClick;
import com.example.yumi.presentation.shared.callbacks.OnAddToPlanButtonClick;
import com.example.yumi.utils.GlideUtil;
import java.util.List;


public class FavoriteMealsRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteMealsRecyclerViewAdapter.FavoriteMealViewHolder> {
    private List<Meal> meals;
    private final OnMealClick onMealClick;
    private final OnAddToPlanButtonClick onAddToPlanButtonClick;
    private final OnDeleteFavClick onDeleteFavClick;

    public FavoriteMealsRecyclerViewAdapter(List<Meal> meals,
                                            OnMealClick onMealClick,
                                            OnAddToPlanButtonClick onAddToPlanButtonClick,
                                            OnDeleteFavClick onDeleteFavClick
    ) {
        this.meals = meals;
        this.onMealClick = onMealClick;
        this.onAddToPlanButtonClick = onAddToPlanButtonClick;
        this.onDeleteFavClick = onDeleteFavClick;
    }

    @NonNull
    @Override
    public FavoriteMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_fav_meal, parent, false);

        return new FavoriteMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMealViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.getMealName().setText(meal.getName());
        holder.getMealCategory().setText(meal.getCategory());
        holder.getAddToPlan().setOnClickListener(v -> onAddToPlanButtonClick.onClick(meal));
        holder.getDeleteBtn().setOnClickListener(v -> onDeleteFavClick.onClick(meal));

        GlideUtil.getImage(
                holder.getView().getContext(),
                holder.getMealImage(),
                meal.getThumbnailUrl()
        );

        holder.getView().setOnClickListener(v -> onMealClick.onClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public static class FavoriteMealViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName;
        private final TextView mealCategory;
        private final ImageView mealImage;
        private final Button addToPlan;
        private final FrameLayout deleteBtn;
        private final View view;

        public FavoriteMealViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            mealName = itemView.findViewById(R.id.fav_meal_title);
            mealCategory = itemView.findViewById(R.id.fav_meal_category);
            mealImage = itemView.findViewById(R.id.fav_meal_image);
            addToPlan = itemView.findViewById(R.id.add_to_plan_btn);
            deleteBtn = itemView.findViewById(R.id.delete_from_fav);
        }

        public FrameLayout getDeleteBtn() {
            return deleteBtn;
        }

        public Button getAddToPlan() {
            return addToPlan;
        }

        public View getView() {
            return view;
        }

        public TextView getMealName() {
            return mealName;
        }

        public TextView getMealCategory() {
            return mealCategory;
        }

        public ImageView getMealImage() {
            return mealImage;
        }
    }
}
