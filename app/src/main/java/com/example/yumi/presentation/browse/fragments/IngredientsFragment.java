package com.example.yumi.presentation.browse.fragments;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
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
import com.example.yumi.databinding.FragmentIngredientsBinding;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.presentation.browse.contracts.BrowseIngredientsContract;
import com.example.yumi.presentation.browse.adapters.IngredientsBrowserAdapter;
import com.example.yumi.presentation.browse.presenter.BrowseIngredientsPresenter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import java.util.ArrayList;
import java.util.List;


public class IngredientsFragment extends Fragment implements BrowseIngredientsContract.View {
    private FragmentIngredientsBinding binding;
    private NavigationCallback navigationCallback;
    private IngredientsBrowserAdapter adapter;
    private BrowseIngredientsPresenter presenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        if (view == null) {
            binding = FragmentIngredientsBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentIngredientsBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new BrowseIngredientsPresenter();
        presenter.attachView(this);

        adapter = new IngredientsBrowserAdapter(new ArrayList<>(),
                category -> navigateToFilteredMeals(category.getName()));

        binding.ingredientsBrowseRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false)
        );

        binding.ingredientsBrowseRecyclerView.setAdapter(adapter);

        binding.backArrowBtn.setOnClickListener(v -> {
            if (isAdded() && !isDetached()) {
                if (navigationCallback != null){
                    navigationCallback.popFragment();
                }
            }
        });

        presenter.loadIngredients();
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        adapter.setIngredients(ingredients);
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

        toggleAllViewsVisibility(VISIBLE);
    }

    @Override
    public void showError(String message) {

    }

    private void toggleAllViewsVisibility(int visibility) {
        binding.ingredientsBrowseRecyclerView.setVisibility(visibility);
    }

    public void navigateToFilteredMeals(String name) {

    }
}