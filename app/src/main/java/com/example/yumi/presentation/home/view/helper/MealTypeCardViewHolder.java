package com.example.yumi.presentation.home.view.helper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.utils.GlideUtil;

public class MealTypeCardViewHolder {
    private final View rootView;
    private final TextView mealTypeName;
    private final TextView mealTypeTime;
    private final LinearLayout emptyStateContainer;
    private final CardView mealContentContainer;
    private final ImageView mealImage;
    private final TextView mealName;
    private final TextView mealCategory;
    private final ImageButton btnRemoveMeal;

    private MealType mealType;
    private OnMealTypeActionListener listener;
    private Meal currentMeal;

    public interface OnMealTypeActionListener {
        void onAddMealClicked(MealType mealType);
        void onRemoveMealClicked(MealType mealType);
        void onMealClicked(Meal meal);
    }

    public MealTypeCardViewHolder(View rootView) {
        this.rootView = rootView;
        mealTypeName = rootView.findViewById(R.id.meal_type_name);
        mealTypeTime = rootView.findViewById(R.id.meal_type_time);
        emptyStateContainer = rootView.findViewById(R.id.empty_state_container);
        mealContentContainer = rootView.findViewById(R.id.meal_content_container);
        mealImage = rootView.findViewById(R.id.meal_image);
        mealName = rootView.findViewById(R.id.meal_name);
        mealCategory = rootView.findViewById(R.id.meal_category);
        btnRemoveMeal = rootView.findViewById(R.id.btn_remove_meal);
    }

    public void setup(MealType mealType, String typeName, String defaultTime,
                      OnMealTypeActionListener listener) {
        this.mealType = mealType;
        this.listener = listener;

        mealTypeName.setText(typeName);
        mealTypeTime.setText(defaultTime);

        emptyStateContainer.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddMealClicked(mealType);
            }
        });

        btnRemoveMeal.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveMealClicked(mealType);
            }
        });
    }

    public void showMeal(Meal meal) {
        if (meal == null) {
            showEmpty();
            return;
        }

        this.currentMeal = meal;

        emptyStateContainer.setVisibility(View.GONE);
        mealContentContainer.setVisibility(View.VISIBLE);

        mealName.setText(meal.getName());
        mealCategory.setText(meal.getCategory());
        GlideUtil.getImage(rootView.getContext(), mealImage, meal.getThumbnailUrl());

        mealContentContainer.setOnClickListener(v -> {
            if (listener != null && currentMeal != null) {
                listener.onMealClicked(currentMeal);
            }
        });
    }

    public void showEmpty() {
        this.currentMeal = null;
        emptyStateContainer.setVisibility(View.VISIBLE);
        mealContentContainer.setVisibility(View.GONE);
    }

    public MealType getMealType() {
        return mealType;
    }

    public Meal getCurrentMeal() {
        return currentMeal;
    }

    public boolean hasMeal() {
        return currentMeal != null;
    }
}