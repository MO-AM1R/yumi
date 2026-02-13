package com.example.yumi.presentation.home.view.fragments;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.yumi.utils.NetworkMonitor.INSTANCE;
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
import com.example.yumi.databinding.FragmentHomeBinding;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.presentation.browse.fragments.CategoriesFragment;
import com.example.yumi.presentation.browse.fragments.CountriesFragment;
import com.example.yumi.presentation.browse.fragments.IngredientsFragment;
import com.example.yumi.presentation.browse.fragments.MealsListFragment;
import com.example.yumi.presentation.custom.AddToPlanBottomSheet;
import com.example.yumi.presentation.details.view.fragment.MealDetailsFragment;
import com.example.yumi.presentation.home.contract.HomeContract;
import com.example.yumi.presentation.home.presenter.HomePresenter;
import com.example.yumi.presentation.home.view.adapters.AreasRecyclerViewAdapter;
import com.example.yumi.presentation.home.view.adapters.CategoriesRecyclerViewAdapter;
import com.example.yumi.presentation.home.view.adapters.IngredientsRecyclerViewAdapter;
import com.example.yumi.presentation.home.view.adapters.RandomMealsRecyclerViewAdapter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import com.example.yumi.utils.GlideUtil;
import com.example.yumi.utils.NetworkMonitor;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements HomeContract.View, NetworkMonitor.NetworkListener {
    private FragmentHomeBinding binding;
    private IngredientsRecyclerViewAdapter ingredientsAdapter;
    private CategoriesRecyclerViewAdapter categoriesAdapter;
    private RandomMealsRecyclerViewAdapter mealsAdapter;
    private AreasRecyclerViewAdapter areasAdapter;
    private HomePresenter presenter;
    private NavigationCallback navigationCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        NetworkMonitor.INSTANCE.addListener(this);
        updateUI();
    }

    private void updateUI() {
        if (NetworkMonitor.INSTANCE.isConnected()) {
            binding.noInternetView.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setVisibility(VISIBLE);
            loadData();
        } else {
            binding.noInternetView.setVisibility(View.VISIBLE);
            binding.swipeRefreshLayout.setVisibility(GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkMonitor.INSTANCE.removeListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new HomePresenter(requireContext().getApplicationContext());

        initAdapters();
        initRecyclerViews();
        initSwipeRefresh();
        initSeeAllButtons();

        presenter.attachView(this);
        loadData();
    }

    private void initSeeAllButtons() {
        binding.countriesSeeAllText.setOnClickListener(v -> {
            if (navigationCallback != null) {
                navigationCallback.navigateToFragment(
                        new CountriesFragment(),
                        "countries"
                );
            }
        });

        binding.categoriesSeeAllText.setOnClickListener(v -> {
            if (navigationCallback != null) {
                navigationCallback.navigateToFragment(
                        new CategoriesFragment(),
                        "categories"
                );
            }
        });

        binding.ingredientsSeeAllText.setOnClickListener(v -> {
            if (navigationCallback != null) {
                navigationCallback.navigateToFragment(
                        new IngredientsFragment(),
                        "ingredients"
                );
            }
        });
    }

    private void showAddToPlanBottomSheet(Meal meal) {
        if (presenter.isGuestMode()){
            showError(getString(R.string.you_are_in_guest_mode));
            return;
        }
        AddToPlanBottomSheet bottomSheet = AddToPlanBottomSheet.newInstance();
        bottomSheet.setOnConfirmListener((date, mealType) -> {
            if (meal != null) {
                presenter.addToMealPlan(meal, date, mealType);
                Snackbar.make(requireView(), getString(R.string.meal_added_plan_successfully), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        bottomSheet.show(getChildFragmentManager(), "addToPlan");
    }

    private void initAdapters() {
        mealsAdapter = new RandomMealsRecyclerViewAdapter(
                meal -> presenter.onMealClicked(meal),
                this::showAddToPlanBottomSheet,
                new ArrayList<>());

        categoriesAdapter = new CategoriesRecyclerViewAdapter(new ArrayList<>(),
                category -> presenter.onCategoryClicked(category));

        areasAdapter = new AreasRecyclerViewAdapter(new ArrayList<>(),
                area -> presenter.onAreaClicked(area)
        );

        ingredientsAdapter = new IngredientsRecyclerViewAdapter(new ArrayList<>(),
                ingredient -> presenter.onIngredientClicked(ingredient));
    }

    private void initRecyclerViews() {
        binding.randomMealsRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
        );
        binding.randomMealsRecyclerView.setAdapter(mealsAdapter);


        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
        );

        binding.categoriesRecyclerView.setAdapter(categoriesAdapter);


        binding.countriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
        );

        binding.countriesRecyclerView.setAdapter(areasAdapter);


        binding.ingredientsRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
        );

        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);
    }

    private void initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(presenter::refreshData);
    }

    private void loadData() {
        presenter.loadUserName();
        presenter.loadHomeData();
    }

    @Override
    public void showDayMeal(Meal meal) {
        if (isDetached()) return;

        binding.mealCategory.setText(meal.getCategory());
        binding.dayMealIngredientsCount.setText(getString(R.string.ingredients_count_message, meal.getIngredients().size()));
        binding.dayMealName.setText(meal.getName());
        binding.addToPlanBtn.getRoot()
                .setOnClickListener(v -> showAddToPlanBottomSheet(meal));

        GlideUtil.getImage(getContext(), binding.mealDayImage, meal.getThumbnailUrl());
        binding.mealDayCard.setVisibility(VISIBLE);
        binding.mealDayCard.setOnClickListener(v -> navigateToMealDetail(meal));
        binding.mealDayCard.setOnClickListener(v -> navigateToMealDetail(meal));
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (!isAdded() || binding == null) return;
        categoriesAdapter.setCategories(categories.subList(0, 10));
    }

    @Override
    public void showAreas(List<Area> areas) {
        if (!isAdded() || binding == null) return;
        areasAdapter.setAreas(areas.subList(0, 10));
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        if (!isAdded() || binding == null) return;
        ingredientsAdapter.setIngredients(ingredients.subList(0, 10));
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
    public void navigateToFilteredMeals(MealsFilter filter) {
        if (navigationCallback != null) {
            navigationCallback.navigateToFragment(
                    new MealsListFragment(filter),
                    "meals"
            );
        }
    }

    @Override
    public void showUserName(String displayName) {
        binding.avatar.setText(displayName);
    }

    @Override
    public void showRandomMeals(List<Meal> meals) {
        if (!isAdded() || binding == null) return;
        mealsAdapter.setMeals(meals);
    }

    @Override
    public void showLoading() {
        if (!isAdded() || binding == null) return;
        binding.loading.setIndeterminate(true);
        binding.loading.setVisibility(VISIBLE);

        toggleAllViewsVisibility(INVISIBLE);
    }

    @Override
    public void hideLoading() {
        if (!isAdded() || binding == null) return;
        binding.loading.setIndeterminate(false);
        binding.loading.setVisibility(GONE);
        binding.swipeRefreshLayout.setRefreshing(false);

        toggleAllViewsVisibility(VISIBLE);
    }

    @Override
    public void showError(String message) {
        if (!isAdded() || binding == null) return;
        Snackbar.make(requireView(), message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private void toggleAllViewsVisibility(int visibility) {
        if (!isAdded() || binding == null) return;
        binding.randomMealsRecyclerView.setVisibility(visibility);
        binding.categoriesRecyclerView.setVisibility(visibility);
        binding.ingredientsRecyclerView.setVisibility(visibility);
        binding.countriesRecyclerView.setVisibility(visibility);
        binding.mealDayCard.setVisibility(visibility);
    }

    @Override
    public void onDestroyView() {
        INSTANCE.removeListener(this);
        presenter.detachView();
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onNetworkAvailable() {
        requireActivity().runOnUiThread(() -> {
            binding.noInternetView.setVisibility(GONE);
            binding.swipeRefreshLayout.setVisibility(VISIBLE);
            loadData();
        });
    }

    @Override
    public void onNetworkLost() {
        requireActivity().runOnUiThread(() -> {
            binding.noInternetView.setVisibility(VISIBLE);
            binding.swipeRefreshLayout.setVisibility(GONE);
        });
    }
}