package com.example.yumi.presentation.home.view.adapters;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yumi.R;
import com.example.yumi.domain.plan.models.PlanDay;
import java.util.ArrayList;
import java.util.List;

public class DaySelectorAdapter extends RecyclerView.Adapter<DaySelectorAdapter.DayViewHolder> {

    private List<PlanDay> days = new ArrayList<>();
    private final OnDayClickListener listener;

    public interface OnDayClickListener {
        void onDayClick(int position);
    }

    public DaySelectorAdapter(OnDayClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDays(List<PlanDay> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    public void updateSelection(int previousPosition, int newPosition) {
        if (previousPosition >= 0 && previousPosition < days.size()) {
            days.get(previousPosition).setSelected(false);
            notifyItemChanged(previousPosition);
        }
        if (newPosition >= 0 && newPosition < days.size()) {
            days.get(newPosition).setSelected(true);
            notifyItemChanged(newPosition);
        }
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_selector, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        PlanDay day = days.get(position);

        holder.dayName.setText(day.getDayName());
        holder.dayNumber.setText(String.valueOf(day.getDayNumber()));

        if (day.isSelected()) {
            holder.container.setBackgroundResource(R.drawable.bg_day_selector_selected);
            holder.dayName.setTextColor(holder.itemView.getContext().getColor(R.color.primary_foreground));
            holder.dayNumber.setTextColor(holder.itemView.getContext().getColor(R.color.primary_foreground));
        } else {
            holder.container.setBackgroundResource(R.drawable.bg_day_selector_unselected);
            holder.dayName.setTextColor(holder.itemView.getContext().getColor(R.color.text_secondary));
            holder.dayNumber.setTextColor(holder.itemView.getContext().getColor(R.color.text_primary));
        }

        holder.container.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDayClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout container;
        final TextView dayName;
        final TextView dayNumber;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.day_container);
            dayName = itemView.findViewById(R.id.day_name);
            dayNumber = itemView.findViewById(R.id.day_number);
        }
    }
}