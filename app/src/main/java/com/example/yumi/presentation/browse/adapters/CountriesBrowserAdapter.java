package com.example.yumi.presentation.browse.adapters;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.presentation.shared.callbacks.OnAreaClick;
import java.util.List;


public class CountriesBrowserAdapter extends RecyclerView.Adapter<CountriesBrowserAdapter.AreaViewHolder> {
    private List<Area> areas;
    private final OnAreaClick onAreaClick;

    public CountriesBrowserAdapter(List<Area> areas, OnAreaClick onAreaClick) {
        this.areas = areas;
        this.onAreaClick = onAreaClick;
    }

    @NonNull
    @Override
    public CountriesBrowserAdapter.AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.area_browse_raw_item, parent, false);

        return new CountriesBrowserAdapter.AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesBrowserAdapter.AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        View view = holder.getItemView();

        holder.getAreaName().setText(area.getName());
        holder.getAreaLogo().setText(area.getName().substring(0, 2).toLowerCase());

        view.setOnClickListener(v -> onAreaClick.onclick(area));
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

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            areaLogo = itemView.findViewById(R.id.area_logo);
            areaName = itemView.findViewById(R.id.area_name);
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
