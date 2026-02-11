package com.example.yumi.presentation.home.view.fragments;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yumi.R;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.yumi.databinding.FragmentMealPlanBinding;
import com.example.yumi.domain.plan.models.PlanDay;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.user.model.MealType;
import com.example.yumi.presentation.details.view.fragment.MealDetailsFragment;
import com.example.yumi.presentation.home.view.adapters.DaySelectorAdapter;
import com.example.yumi.presentation.home.contract.MealPlanContract;
import com.example.yumi.presentation.home.presenter.MealPlanPresenter;
import com.example.yumi.presentation.home.view.helper.MealTypeCardViewHolder;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;

import java.util.List;

public class MealPlanFragment extends Fragment implements MealPlanContract.View,
        MealTypeCardViewHolder.OnMealTypeActionListener {

    private FragmentMealPlanBinding binding;
    private MealPlanPresenter presenter;
    private DaySelectorAdapter daySelectorAdapter;
    private NavigationCallback navigationCallback;

    private MealTypeCardViewHolder breakfastCard;
    private MealTypeCardViewHolder lunchCard;
    private MealTypeCardViewHolder dinnerCard;
    private MealTypeCardViewHolder snackCard;

    public MealPlanFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMealPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new MealPlanPresenter(requireContext().getApplicationContext(), this);

        setupDaysRecyclerView();
        setupMealTypeCards();

        presenter.loadMealPlan();
    }

    private void setupDaysRecyclerView() {
        daySelectorAdapter = new DaySelectorAdapter(position -> presenter.onDaySelected(position));

        binding.daysRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.daysRecyclerView.setAdapter(daySelectorAdapter);
    }

    private void setupMealTypeCards() {
        breakfastCard = new MealTypeCardViewHolder(binding.breakfastCard.getRoot());
        breakfastCard.setup(
                MealType.BREAKFAST,
                getString(R.string.breakfast),
                getString(R.string.breakfast_time),
                this
        );

        lunchCard = new MealTypeCardViewHolder(binding.lunchCard.getRoot());
        lunchCard.setup(
                MealType.LUNCH,
                getString(R.string.lunch),
                getString(R.string.lunch_time),
                this
        );

        dinnerCard = new MealTypeCardViewHolder(binding.dinnerCard.getRoot());
        dinnerCard.setup(
                MealType.DINNER,
                getString(R.string.dinner),
                getString(R.string.dinner_time),
                this
        );

        snackCard = new MealTypeCardViewHolder(binding.snackCard.getRoot());
        snackCard.setup(
                MealType.SNACK,
                getString(R.string.snack),
                getString(R.string.snack_time),
                this
        );
    }

    @Override
    public void showDays(List<PlanDay> days) {
        if (!isAdded() || binding == null) return;
        daySelectorAdapter.setDays(days);
    }

    @Override
    public void showSelectedDate(String fullDate) {
        if (!isAdded() || binding == null) return;
        binding.selectedDateText.setText(fullDate);
    }

    @Override
    public void showMealForType(MealType mealType, Meal meal) {
        if (!isAdded() || binding == null) return;

        switch (mealType) {
            case BREAKFAST:
                breakfastCard.showMeal(meal);
                break;
            case LUNCH:
                lunchCard.showMeal(meal);
                break;
            case DINNER:
                dinnerCard.showMeal(meal);
                break;
            case SNACK:
                snackCard.showMeal(meal);
                break;
        }
    }

    @Override
    public void showEmptyMealType(MealType mealType) {
        if (!isAdded() || binding == null) return;

        switch (mealType) {
            case BREAKFAST:
                breakfastCard.showEmpty();
                break;
            case LUNCH:
                lunchCard.showEmpty();
                break;
            case DINNER:
                dinnerCard.showEmpty();
                break;
            case SNACK:
                snackCard.showEmpty();
                break;
        }
    }

    @Override
    public void updateDaySelection(int previousPosition, int newPosition) {
        if (!isAdded() || binding == null) return;
        daySelectorAdapter.updateSelection(previousPosition, newPosition);
    }

    @Override
    public void showMealRemovedSuccess() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), R.string.meal_removed_from_plan, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMealAddedSuccess() {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), R.string.meal_added_to_plan, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToAddMeal(String date, MealType mealType) {
        // TODO: Navigate to meal selection screen or show favorites selection bottom sheet
        Toast.makeText(
                requireContext(),
                getString(R.string.select_meal_for_type, mealType.name()),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        if (navigationCallback != null) {
            navigationCallback.navigateToFragment(
                    new MealDetailsFragment(meal),
                    "meal_details"
            );
        }
    }

    @Override
    public void showLoading() {
        if (!isAdded() || binding == null) return;
        binding.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (!isAdded() || binding == null) return;
        binding.loading.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddMealClicked(MealType mealType) {
        presenter.onAddMealClicked(mealType);
    }

    @Override
    public void onRemoveMealClicked(MealType mealType) {
        presenter.onRemoveMealClicked(mealType);
    }

    @Override
    public void onMealClicked(Meal meal) {
        navigateToMealDetails(meal);
    }

    public void addMealToPlan(String dateKey, MealType mealType, Meal meal) {
        if (presenter != null) {
            presenter.addMealToPlan(dateKey, mealType, meal);
        }
    }

    public String getSelectedDateKey() {
        if (presenter != null) {
            return presenter.getSelectedDateKey();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        Log.d("TAG", "On Destroy");
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationCallback = null;
    }
}