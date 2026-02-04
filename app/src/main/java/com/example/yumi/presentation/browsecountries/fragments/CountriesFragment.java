package com.example.yumi.presentation.browsecountries.fragments;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentCountriesBinding;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.presentation.browsecountries.BrowseCountriesContract;
import com.example.yumi.presentation.browsecountries.adapters.CountriesBrowserAdapter;
import com.example.yumi.presentation.browsecountries.presenter.BrowseCountriesPresenter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import java.util.ArrayList;
import java.util.List;


public class CountriesFragment extends Fragment implements BrowseCountriesContract.View {
    private FragmentCountriesBinding binding;
    private CountriesBrowserAdapter countriesBrowserAdapter;
    private BrowseCountriesPresenter presenter;

    private NavigationCallback navigationCallback;

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

        View view = inflater.inflate(R.layout.fragment_countries, container, false);

        if (view == null) {
            binding = FragmentCountriesBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentCountriesBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new BrowseCountriesPresenter();
        presenter.attachView(this);

        countriesBrowserAdapter = new CountriesBrowserAdapter(new ArrayList<>(),
                area -> navigateToFilteredMeals(area.getName())
        );

        binding.countriesBrowseRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false)
        );

        binding.countriesBrowseRecyclerView.setAdapter(countriesBrowserAdapter);

        binding.backArrowBtn.setOnClickListener(v -> {
            if (isAdded() && !isDetached()) {
                if (navigationCallback != null){
                    navigationCallback.popFragment();
                }
            }
        });

        presenter.loadAreas();
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
        binding.countriesBrowseRecyclerView.setVisibility(visibility);
    }

    @Override
    public void showAreas(List<Area> areas) {
        countriesBrowserAdapter.setAreas(areas);
    }

    public void navigateToFilteredMeals(String name) {

    }
}