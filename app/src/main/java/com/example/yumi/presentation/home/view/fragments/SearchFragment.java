package com.example.yumi.presentation.home.view.fragments;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.yumi.databinding.FragmentSearchBinding;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.home.view.adapters.AreaSearchGridViewAdapter;
import com.example.yumi.presentation.home.view.adapters.CategorySearchGridViewAdapter;
import com.example.yumi.presentation.home.view.adapters.IngredientSearchGridViewAdapter;
import com.example.yumi.presentation.home.view.adapters.MealSearchGridView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private CategorySearchGridViewAdapter categoryAdapter;
    private AreaSearchGridViewAdapter areaAdapter;
    private IngredientSearchGridViewAdapter ingredientAdapter;
    private MealSearchGridView mealAdapter;
    private List<Category> allCategories = new ArrayList<>();
    private List<Area> allAreas = new ArrayList<>();
    private List<Ingredient> allIngredients = new ArrayList<>();
    private List<Meal> allMeals = new ArrayList<>();
    private int currentTabIndex = 0;
    private static final long SEARCH_DEBOUNCE_MS = 300;

    private static final int TAB_CATEGORIES = 0;
    private static final int TAB_COUNTRIES = 1;
    private static final int TAB_INGREDIENTS = 2;
    private static final int TAB_MEALS = 3;
    private static final String[] SEARCH_HINTS = {
            "Search categories...",
            "Search countries...",
            "Search ingredients...",
            "Search meals..."
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupAdapters();
        setupTabs();
        setupSearch();

        loadDataForTab(TAB_MEALS);
    }

    private void setupAdapters() {
        categoryAdapter = new CategorySearchGridViewAdapter(this::onCategoryClick);
        areaAdapter = new AreaSearchGridViewAdapter(this::onAreaClick);
        ingredientAdapter = new IngredientSearchGridViewAdapter(this::onIngredientClick);
        mealAdapter = new MealSearchGridView(this::onMealClick, this::onMealAddClick);

        binding.gridView.setAdapter(categoryAdapter);
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Categories"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Countries"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ingredients"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Meals"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabIndex = tab.getPosition();
                updateSearchHint();
                clearSearch();
                setupViewForTab(currentTabIndex);
                loadDataForTab(currentTabIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewForTab(int tabIndex) {
        switch (tabIndex) {
            case TAB_CATEGORIES:
                binding.gridView.setNumColumns(3);
                binding.gridView.setAdapter(categoryAdapter);
                binding.gridView.setVisibility(View.VISIBLE);
                binding.listView.setVisibility(View.GONE);
                break;
            case TAB_COUNTRIES:
                binding.gridView.setNumColumns(2);
                binding.gridView.setAdapter(areaAdapter);
                binding.gridView.setVisibility(View.VISIBLE);
                binding.listView.setVisibility(View.GONE);
                break;
            case TAB_INGREDIENTS:
                binding.gridView.setNumColumns(3);
                binding.gridView.setAdapter(ingredientAdapter);
                binding.gridView.setVisibility(View.VISIBLE);
                binding.listView.setVisibility(View.GONE);
                break;
            case TAB_MEALS:
                binding.listView.setAdapter(mealAdapter);
                binding.gridView.setVisibility(View.GONE);
                binding.listView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                performSearch(Objects.requireNonNull(binding.etSearch.getText()).toString().trim());
                return true;
            }
            return false;
        });
    }

    private void updateSearchHint() {
        binding.etSearch.setHint(SEARCH_HINTS[currentTabIndex]);
    }

    private void clearSearch() {
        binding.etSearch.setText("");
    }

    private void performSearch(String query) {
        switch (currentTabIndex) {
            case TAB_CATEGORIES:
                filterCategories(query);
                break;
            case TAB_COUNTRIES:
                filterAreas(query);
                break;
            case TAB_INGREDIENTS:
                filterIngredients(query);
                break;
            case TAB_MEALS:
                if (query.isEmpty()) {
                    mealAdapter.setMeals(allMeals);
                } else {
                    searchMeals(query);
                }
                break;
        }
    }

    private void filterCategories(String query) {
        if (query.isEmpty()) {
            categoryAdapter.setCategories(allCategories);
            hideEmptyState();
            return;
        }

        List<Category> filtered = new ArrayList<>();
        for (Category category : allCategories) {
            if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(category);
            }
        }

        categoryAdapter.setCategories(filtered);
        if (filtered.isEmpty()) {
            showEmptyState("No categories found");
        } else {
            hideEmptyState();
        }
    }

    private void filterAreas(String query) {
        if (query.isEmpty()) {
            areaAdapter.setAreas(allAreas);
            hideEmptyState();
            return;
        }

        List<Area> filtered = new ArrayList<>();
        for (Area area : allAreas) {
            if (area.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(area);
            }
        }

        areaAdapter.setAreas(filtered);
        if (filtered.isEmpty()) {
            showEmptyState("No countries found");
        } else {
            hideEmptyState();
        }
    }

    private void filterIngredients(String query) {
        if (query.isEmpty()) {
            ingredientAdapter.setIngredients(allIngredients);
            hideEmptyState();
            return;
        }

        List<Ingredient> filtered = new ArrayList<>();
        for (Ingredient ingredient : allIngredients) {
            if (ingredient.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(ingredient);
            }
        }

        ingredientAdapter.setIngredients(filtered);
        if (filtered.isEmpty()) {
            showEmptyState("No ingredients found");
        } else {
            hideEmptyState();
        }
    }

    private void searchMeals(String query) {
        // TODO: Call presenter to search meals by name from API
    }

    private void loadDataForTab(int tabIndex) {
        // TODO: Call your presenter to load data
    }

    private void onCategoryClick(Category category) {
        // TODO: Navigate to category meals list
    }

    private void onAreaClick(Area area) {
        // TODO: Navigate to area meals list
    }

    private void onIngredientClick(Ingredient ingredient) {
        // TODO: Navigate to ingredient meals list
    }

    private void onMealClick(Meal meal) {
        // TODO: Navigate to meal details
    }

    private void onMealAddClick(Meal meal) {
        // TODO: Add meal to plan/favorites
    }

    public void showCategories(List<Category> categories) {
        hideLoading();
        allCategories = new ArrayList<>(categories);

        if (categories.isEmpty()) {
            showEmptyState("No categories found");
        } else {
            hideEmptyState();
            categoryAdapter.setCategories(categories);
        }
    }

    public void showAreas(List<Area> areas) {
        hideLoading();
        allAreas = new ArrayList<>(areas);

        if (areas.isEmpty()) {
            showEmptyState("No countries found");
        } else {
            hideEmptyState();
            areaAdapter.setAreas(areas);
        }
    }

    public void showIngredients(List<Ingredient> ingredients) {
        hideLoading();
        allIngredients = new ArrayList<>(ingredients);

        if (ingredients.isEmpty()) {
            showEmptyState("No ingredients found");
        } else {
            hideEmptyState();
            ingredientAdapter.setIngredients(ingredients);
        }
    }

    public void showMeals(List<Meal> meals) {
        hideLoading();
        allMeals = new ArrayList<>(meals);

        if (meals.isEmpty()) {
            showEmptyState("No meals found");
        } else {
            hideEmptyState();
            mealAdapter.setMeals(meals);
        }
    }

    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.gridView.setVisibility(View.GONE);
        binding.listView.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.GONE);
    }

    public void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);

        if (currentTabIndex == TAB_MEALS) {
            binding.listView.setVisibility(View.VISIBLE);
        } else {
            binding.gridView.setVisibility(View.VISIBLE);
        }
    }

    public void showEmptyState(String message) {
        binding.emptyState.setVisibility(View.VISIBLE);
        binding.tvEmptyMessage.setText(message);
        binding.gridView.setVisibility(View.GONE);
        binding.listView.setVisibility(View.GONE);
    }

    public void hideEmptyState() {
        binding.emptyState.setVisibility(View.GONE);

        if (currentTabIndex == TAB_MEALS) {
            binding.listView.setVisibility(View.VISIBLE);
        } else {
            binding.gridView.setVisibility(View.VISIBLE);
        }
    }

    public void showError(String message) {
        hideLoading();
        showEmptyState(message);
    }

    private void hideKeyboard() {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}