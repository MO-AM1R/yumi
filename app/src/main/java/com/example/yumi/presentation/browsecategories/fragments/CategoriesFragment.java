package com.example.yumi.presentation.browsecategories.fragments;
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
import com.example.yumi.databinding.FragmentCategoriesBinding;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.presentation.browsecategories.BrowseCategoriesContract;
import com.example.yumi.presentation.browsecategories.adapters.CategoriesBrowserAdapter;
import com.example.yumi.presentation.browsecategories.presenter.BrowseCategoriesPresenter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment implements BrowseCategoriesContract.View {
    private FragmentCategoriesBinding binding;
    private NavigationCallback navigationCallback;
    private CategoriesBrowserAdapter adapter;
    private BrowseCategoriesPresenter presenter;

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
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        if (view == null) {
            binding = FragmentCategoriesBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentCategoriesBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new BrowseCategoriesPresenter();
        presenter.attachView(this);

        adapter = new CategoriesBrowserAdapter(new ArrayList<>(),
                category -> navigateToFilteredMeals(category.getName()));

        binding.categoriesBrowseRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false)
        );

        binding.categoriesBrowseRecyclerView.setAdapter(adapter);

        binding.backArrowBtn.setOnClickListener(v -> {
            if (isAdded() && !isDetached()) {
                if (navigationCallback != null){
                    navigationCallback.popFragment();
                }
            }
        });

        presenter.loadCategories();
    }

    @Override
    public void showCategories(List<Category> categories) {
        adapter.setCategories(categories);
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
        binding.categoriesBrowseRecyclerView.setVisibility(visibility);
    }

    public void navigateToFilteredMeals(String name) {

    }
}