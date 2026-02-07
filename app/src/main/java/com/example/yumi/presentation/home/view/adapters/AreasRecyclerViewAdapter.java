package com.example.yumi.presentation.home.view.adapters;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.presentation.shared.callbacks.OnAreaClick;

import java.util.List;


public class AreasRecyclerViewAdapter extends RecyclerView.Adapter<AreasRecyclerViewAdapter.AreaViewHolder> {
    private List<Area> areas;
    private final OnAreaClick onAreaClick;

    public AreasRecyclerViewAdapter(List<Area> areas, OnAreaClick onAreaClick) {
        this.areas = areas;
        this.onAreaClick = onAreaClick;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.area_raw_item, parent, false);

        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        View view = holder.getItemView();

        int paddingEnd = 0;
        if (position < areas.size() - 1){
            paddingEnd = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16,
                    view.getResources().getDisplayMetrics()
            );
        }

        view.setPadding(0, 0, paddingEnd, 0);

        holder.getAreaName().setText(area.getName());
        holder.getAreaLogo().setText(area.getName().substring(0, 2).toLowerCase());

        holder.getCardView().setOnClickListener(v -> onAreaClick.onClick(area));
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setAreas(List<Area> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        private final TextView areaLogo;
        private final TextView areaName;
        private final View itemView;
        private final CardView cardView;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            areaLogo = itemView.findViewById(R.id.area_logo);
            areaName = itemView.findViewById(R.id.area_name);
            cardView = itemView.findViewById(R.id.category_card_view);
        }

        public CardView getCardView() {
            return cardView;
        }

        public View getItemView() {
            return itemView;
        }

        public TextView getAreaLogo() {
            return areaLogo;
        }

        public TextView getAreaName() {
            return areaName;
        }
    }
}
