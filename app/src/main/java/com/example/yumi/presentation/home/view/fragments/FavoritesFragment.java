package com.example.yumi.presentation.home.view.fragments;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentFavoritesBinding;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.custom.AddToPlanBottomSheet;
import com.example.yumi.presentation.details.view.fragment.MealDetailsFragment;
import com.example.yumi.presentation.home.contract.FavoriteContract;
import com.example.yumi.presentation.home.presenter.FavoritePresenter;
import com.example.yumi.presentation.home.view.adapters.FavoriteMealsRecyclerViewAdapter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment implements FavoriteContract.View {
    private FragmentFavoritesBinding binding;
    private FavoritePresenter favoritePresenter;
    private FavoriteMealsRecyclerViewAdapter adapter;
    private NavigationCallback navigationCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPresenter();

        if (favoritePresenter.isGuestMode()){
            showGuestMode();
        }else{
            setupRecyclerView();
            favoritePresenter.loadFavoriteMeals();
        }
    }

    private void showGuestMode() {
        binding.guestMode.setVisibility(VISIBLE);
        binding.favoritesContent.setVisibility(GONE);
    }

    private void setupPresenter() {
        favoritePresenter = new FavoritePresenter(
                requireActivity().getApplicationContext(),
                this);
    }

    private void setupRecyclerView() {
        adapter = new FavoriteMealsRecyclerViewAdapter(
                new ArrayList<>(),
                this::navigateToMealDetail,
                this::showAddToPlanBottomSheet,
                meal -> favoritePresenter.onMealRemoved(meal)
        );

        binding.favoriteRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false)
        );
        binding.favoriteRecyclerView.setAdapter(adapter);
    }

    private void showAddToPlanBottomSheet(Meal meal) {
        AddToPlanBottomSheet bottomSheet = AddToPlanBottomSheet.newInstance();
        bottomSheet.setOnConfirmListener((date, mealType) -> {
            if (meal != null) {
                favoritePresenter.addToMealPlan(meal, date, mealType);
            }
        });
        bottomSheet.show(getChildFragmentManager(), "addToPlan");
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        binding.favMealsCount.setText(
                getString(R.string.recipe_saved, meals.size())
        );

        if (!meals.isEmpty()) {
            adapter.setMeals(meals);
            binding.favoriteRecyclerView.setVisibility(VISIBLE);
            binding.noFavoriteMessage.setVisibility(GONE);
        } else {
            binding.favoriteRecyclerView.setVisibility(GONE);
            binding.noFavoriteMessage.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoritePresenter.detachView();
        binding = null;
    }

    @Override
    public void navigateToMealDetail(Meal meal) {
        if (navigationCallback != null) {
            navigationCallback.navigateToFragment(
                    new MealDetailsFragment(meal),
                    "meals"
            );
        }
    }

    @Override
    public void showLoading() {
        binding.loading.setVisibility(VISIBLE);
        binding.loading.setIndeterminate(true);
    }

    @Override
    public void hideLoading() {
        binding.loading.setVisibility(GONE);
        binding.loading.setIndeterminate(false);
    }

    @Override
    public void showError(String message) {

    }
}