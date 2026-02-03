package com.example.yumi.presentation.home.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumi.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView randomMealsRecyclerView;
    private RecyclerView countriesRecyclerView;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView ingredientsRecyclerView;

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

        setupRandomMealRecyclerView();
    }

    void setupRandomMealRecyclerView(){
        randomMealsRecyclerView = binding.randomMealsRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        randomMealsRecyclerView.setLayoutManager(layoutManager);
//        randomMealsRecyclerView.setAdapter();
    }
}