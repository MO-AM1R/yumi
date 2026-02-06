package com.example.yumi.presentation.browse.fragments;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentMealsListBinding;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.model.MealsFilter;
import com.example.yumi.presentation.browse.adapters.MealsGridAdapter;
import com.example.yumi.presentation.browse.contracts.MealsListContract;
import com.example.yumi.presentation.browse.presenter.MealsListPresenter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import java.util.List;
import android.os.Bundle;


public class MealsListFragment extends Fragment implements MealsListContract.View {
    private NavigationCallback navigationCallback;
    private FragmentMealsListBinding binding;
    private MealsListPresenter presenter;
    private MealsGridAdapter adapter;
    private final MealsFilter filter;

    public MealsListFragment(MealsFilter filter) {
        this.filter = filter;
    }

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

        View view = inflater.inflate(R.layout.fragment_meals_list, container, false);
        if (view == null) {
            binding = FragmentMealsListBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentMealsListBinding.bind(view);
        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MealsListPresenter(filter,this);
        presenter.attachView(this);

        adapter = new MealsGridAdapter(
                meal -> presenter.onMealClicked(meal),
                meal -> presenter.onAddToFavClicked(meal),
                meal -> presenter.onAddToPlanClicked(meal)
        );

        binding.mealsGridView.setAdapter(adapter);
        binding.filterTitle.setText(filter.toString());

        binding.backArrowBtn.setOnClickListener(v -> {
            if (isAdded() && !isDetached()) {
                if (navigationCallback != null) {
                    navigationCallback.popFragment();
                }
            }
        });

        presenter.loadMeals();
    }

    @Override
    public void showMeals(List<Meal> meals) {
        binding.mealsListCount.setText(getString(R.string.meals_list_count, meals.size()));
        adapter.setMeals(meals);
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
        binding.mealsGridView.setVisibility(visibility);
    }
}