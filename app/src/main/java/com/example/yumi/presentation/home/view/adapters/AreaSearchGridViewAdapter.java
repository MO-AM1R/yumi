package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.yumi.R;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.presentation.shared.callbacks.OnAreaClick;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;


public class AreaSearchGridViewAdapter extends BaseAdapter {
    private List<Area> areas = new ArrayList<>();
    private final OnAreaClick onAreaClick;

    public AreaSearchGridViewAdapter(OnAreaClick onAreaClick) {
        this.onAreaClick = onAreaClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAreas(List<Area> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public Area getItem(int position) {
        return areas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AreaViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_area_search_row, parent, false);
            holder = new AreaViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AreaViewHolder) convertView.getTag();
        }

        Area area = getItem(position);
        String areaName = area.getName();
        String areaCode = areaName.substring(0, Math.min(2, areaName.length())).toUpperCase();

        holder.tvAreaCode.setText(areaCode);
        holder.tvAreaName.setText(areaName);

        holder.cardArea.setOnClickListener(v -> {
            if (onAreaClick != null) {
                onAreaClick.onClick(area);
            }
        });

        return convertView;
    }

    private static class AreaViewHolder {
        private final MaterialCardView cardArea;
        private final TextView tvAreaCode;
        private final TextView tvAreaName;

        AreaViewHolder(View itemView) {
            cardArea = itemView.findViewById(R.id.card_area);
            tvAreaCode = itemView.findViewById(R.id.tv_area_code);
            tvAreaName = itemView.findViewById(R.id.tv_area_name);
        }
    }
}