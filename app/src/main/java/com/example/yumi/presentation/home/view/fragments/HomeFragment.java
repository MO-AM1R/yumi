package com.example.yumi.presentation.home.view.fragments;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumi.R;
import com.example.yumi.databinding.FragmentHomeBinding;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.HomeContract;
import com.example.yumi.presentation.home.presenter.HomePresenter;
import com.example.yumi.presentation.home.view.adapters.RandomMealsRecyclerViewAdapter;
import com.example.yumi.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements HomeContract.View {
    private FragmentHomeBinding binding;
    private RecyclerView randomMealsRecyclerView;
    private RecyclerView countriesRecyclerView;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView ingredientsRecyclerView;
    private RandomMealsRecyclerViewAdapter mealsAdapter;
    //    private CategoriesAdapter categoriesAdapter;
//    private AreasAdapter areasAdapter;
    private HomePresenter presenter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            binding = FragmentHomeBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentHomeBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new HomePresenter();

        initAdapters();
        initRecyclerViews();
        initSwipeRefresh();

        presenter.attachView(this);
        loadData();
    }

    private void initAdapters() {
        mealsAdapter = new RandomMealsRecyclerViewAdapter(meal -> presenter.onMealClicked(meal), new ArrayList<>());

//        categoriesAdapter = new CategoriesAdapter(category ->
//                presenter.filterByCategory(category.getName())
//        );

//        areasAdapter = new AreasAdapter(area ->
//                presenter.filterByArea(area.getName())
//        );
    }

    private void initRecyclerViews() {
        // Meals RecyclerView - Grid
//        binding.recyclerViewMeals.setLayoutManager(
//                new GridLayoutManager(requireContext(), 2)
//        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.randomMealsRecyclerView.setLayoutManager(layoutManager);

        binding.randomMealsRecyclerView.setAdapter(mealsAdapter);

        // Categories RecyclerView - Horizontal
//        binding.recyclerViewCategories.setLayoutManager(
//                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        );
//        binding.recyclerViewCategories.setAdapter(categoriesAdapter);

        // Areas RecyclerView - Horizontal
//        binding.recyclerViewAreas.setLayoutManager(
//                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        );
//        binding.recyclerViewAreas.setAdapter(areasAdapter);
    }

    private void initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        presenter.loadDayMeal();
        presenter.loadRandomMeals();
        presenter.loadCategories();
        presenter.loadAreas();
    }

    @Override
    public void showDayMeal(Meal meal) {
        binding.mealCategory.setText(meal.getCategory());
        binding.dayMealIngredientsCount.setText(getString(R.string.ingredients_count_message, meal.getIngredients().size()));
        binding.dayMealName.setText(meal.getName());

        GlideUtil.getImage(getContext(), binding.mealDayImage, meal.getThumbnailUrl());
        binding.mealDayCard.setVisibility(VISIBLE);
    }

    @Override
    public void showRandomMeals(List<Meal> meals) {
        mealsAdapter.setMeals(meals);
    }

    @Override
    public void showCategories(List<Category> categories) {

    }

    @Override
    public void showAreas(List<Area> areas) {

    }

    @Override
    public void navigateToMealDetail(String mealId) {

    }

    @Override
    public void showLoading() {
        binding.loading.setIndeterminate(true);
        binding.loading.setVisibility(VISIBLE);

        toggleAllViewsVisibility(INVISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.loading.setIndeterminate(false);
        binding.loading.setVisibility(GONE);
        binding.swipeRefreshLayout.setRefreshing(false);

        toggleAllViewsVisibility(VISIBLE);
    }

    @Override
    public void showError(String message) {

    }

    private void toggleAllViewsVisibility(int visibility) {
        binding.randomMealsRecyclerView.setVisibility(visibility);
        binding.categoriesRecyclerView.setVisibility(visibility);
        binding.ingredientsRecyclerView.setVisibility(visibility);
        binding.countriesRecyclerView.setVisibility(visibility);
        binding.mealDayCard.setVisibility(visibility);
    }
}