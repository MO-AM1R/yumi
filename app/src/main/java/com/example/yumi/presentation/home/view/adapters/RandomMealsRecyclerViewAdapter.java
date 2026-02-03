package com.example.yumi.presentation.home.view.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RandomMealsRecyclerViewAdapter extends RecyclerView.Adapter<RandomMealsRecyclerViewAdapter.RandomMealsViewHolder> {


    public RandomMealsRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public RandomMealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RandomMealsViewHolder holder, int position) {

    }

    public static class RandomMealsViewHolder extends RecyclerView.ViewHolder {

        public RandomMealsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
