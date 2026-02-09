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

import com.example.yumi.R;
import com.example.yumi.databinding.FragmentSearchBinding;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.presentation.browse.fragments.MealsListFragment;
import com.example.yumi.presentation.details.view.fragment.MealDetailsFragment;
import com.example.yumi.presentation.home.contract.SearchContract;
import com.example.yumi.presentation.home.presenter.SearchPresenter;
import com.example.yumi.presentation.home.view.adapters.AreaSearchGridViewAdapter;
import com.example.yumi.presentation.home.view.adapters.CategorySearchGridViewAdapter;
import com.example.yumi.presentation.home.view.adapters.IngredientSearchGridViewAdapter;
import com.example.yumi.presentation.home.view.adapters.MealSearchGridView;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;


public class SearchFragment extends Fragment implements SearchContract.View {
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
    private final int TAB_CATEGORIES = 0;
    private final int TAB_COUNTRIES = 1;
    private final int TAB_INGREDIENTS = 2;
    private final int TAB_MEALS = 3;
    private final String CURRENT_TAB_KEY = "current_tab";

    private final String[] SEARCH_HINTS = {
            "Search categories...",
            "Search countries...",
            "Search ingredients...",
            "Search meals..."
    };

    private SearchPresenter presenter;
    private NavigationCallback navigationCallback;
    private Disposable disposable;


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
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SearchPresenter(this);
        presenter.attachView(this);

        setupAdapters();
        setupTabs();
        setupSearch();

        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt(CURRENT_TAB_KEY);
        }

        Objects.requireNonNull(binding.tabLayout.getTabAt(currentTabIndex)).select();
        setupViewForTab(currentTabIndex);
        loadDataForTab(currentTabIndex);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_TAB_KEY, currentTabIndex);
    }

    private void setupAdapters() {
        categoryAdapter = new CategorySearchGridViewAdapter(category ->
                presenter.onCategoryClicked(category));

        areaAdapter = new AreaSearchGridViewAdapter(
                area -> presenter.onAreaClicked(area)
        );

        ingredientAdapter = new IngredientSearchGridViewAdapter(
                ingredient -> presenter.onIngredientClicked(ingredient)
        );

        mealAdapter = new MealSearchGridView(
                meal -> presenter.onMealClicked(meal),
                this::onMealAddClick);

        binding.gridView.setAdapter(categoryAdapter);
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireContext().getString(R.string.categories)
        ));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireContext().getString(R.string.countries)
        ));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireContext().getString(R.string.ingredients)
        ));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireContext().getString(R.string.meals)
        ));

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
        binding.etSearch.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard();
                        return true;
                    }
                    return false;
                });

        Observable<String> observable = Observable.create(emitter -> {
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    emitter.onNext(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            };

            binding.etSearch.addTextChangedListener(textWatcher);
            emitter.setCancellable(() -> binding.etSearch.removeTextChangedListener(textWatcher));
        });

        long DEBOUNCE_DELAY = 500;
        disposable =
                observable.debounce(DEBOUNCE_DELAY, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::performSearch);
    }

    private void updateSearchHint() {
        if (!isAdded() || binding == null) return;
        binding.etSearch.setHint(SEARCH_HINTS[currentTabIndex]);
    }

    private void clearSearch() {
        if (!isAdded() || binding == null) return;
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
        if (!isAdded() || binding == null) return;
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
        if (!isAdded() || binding == null) return;
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
        if (!isAdded() || binding == null) return;
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
        if (!isAdded() || binding == null) return;
        presenter.loadMeals(query);
    }

    private void loadDataForTab(int tabIndex) {
        if (!isAdded() || binding == null) return;
        switch (tabIndex) {
            case TAB_CATEGORIES:
                presenter.loadCategories();
                break;
            case TAB_COUNTRIES:
                presenter.loadAreas();
                break;
            case TAB_INGREDIENTS:
                presenter.loadIngredients();
                break;
            case TAB_MEALS:
                presenter.loadMeals(Objects.requireNonNull(binding.etSearch.getText()).toString());
                break;
        }
    }

    private void onMealAddClick(Meal meal) {
        // TODO: Add meal to plan/favorites
    }

    public void showCategories(List<Category> categories) {
        if (!isAdded() || binding == null) return;
        allCategories = new ArrayList<>(categories);

        if (categories.isEmpty()) {
            showEmptyState("No categories found");
        } else {
            hideEmptyState();
            categoryAdapter.setCategories(categories);
        }
    }

    public void showAreas(List<Area> areas) {
        if (!isAdded() || binding == null) return;
        allAreas = new ArrayList<>(areas);

        if (areas.isEmpty()) {
            showEmptyState("No countries found");
        } else {
            hideEmptyState();
            areaAdapter.setAreas(areas);
        }
    }

    public void showIngredients(List<Ingredient> ingredients) {
        if (!isAdded() || binding == null) return;
        allIngredients = new ArrayList<>(ingredients);

        if (ingredients.isEmpty()) {
            showEmptyState("No ingredients found");
        } else {
            hideEmptyState();
            ingredientAdapter.setIngredients(ingredients);
        }
    }

    @Override
    public void navigateToMealDetail(Meal meal) {
        if (!isAdded() || binding == null) return;
        if (navigationCallback != null) {
            navigationCallback.navigateToFragment(
                    new MealDetailsFragment(meal),
                    "meals"
            );
        }
    }

    @Override
    public void navigateToFilteredMeals(MealsFilter filter) {
        if (!isAdded() || binding == null) return;
        if (navigationCallback != null) {
            navigationCallback.navigateToFragment(
                    new MealsListFragment(filter),
                    "meals"
            );
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (!isAdded() || binding == null) return;
        allMeals = new ArrayList<>(meals);

        if (meals.isEmpty()) {
            showEmptyState("No meals found");
        } else {
            hideEmptyState();
            mealAdapter.setMeals(meals);
        }
    }

    @Override
    public void showLoading() {
        if (!isAdded() || binding == null) return;
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.gridView.setVisibility(View.GONE);
        binding.listView.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (!isAdded() || binding == null) return;
        binding.progressBar.setVisibility(View.GONE);

        if (currentTabIndex == TAB_MEALS) {
            binding.listView.setVisibility(View.VISIBLE);
        } else {
            binding.gridView.setVisibility(View.VISIBLE);
        }
    }

    public void showEmptyState(String message) {
        if (!isAdded() || binding == null) return;
        binding.emptyState.setVisibility(View.VISIBLE);
        binding.tvEmptyMessage.setText(message);
        binding.gridView.setVisibility(View.GONE);
        binding.listView.setVisibility(View.GONE);
    }

    public void hideEmptyState() {
        if (!isAdded() || binding == null) return;
        binding.emptyState.setVisibility(View.GONE);

        if (currentTabIndex == TAB_MEALS) {
            binding.listView.setVisibility(View.VISIBLE);
        } else {
            binding.gridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        if (!isAdded() || binding == null) return;
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
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
        if (presenter != null)
            presenter.detachView();


        binding = null;
    }
}