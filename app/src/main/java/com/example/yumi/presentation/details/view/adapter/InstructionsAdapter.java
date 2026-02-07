package com.example.yumi.presentation.details.view.adapter;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yumi.R;
import java.util.ArrayList;
import java.util.List;


public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.InstructionViewHolder> {
    private final List<String> instructions = new ArrayList<>();

    public InstructionsAdapter() {}

    @SuppressLint("NotifyDataSetChanged")
    public void setInstructions(String[] instructionsArray) {
        this.instructions.clear();
        for (String instruction : instructionsArray) {
            String trimmed = instruction.trim();
            if (!trimmed.isEmpty()) {
                this.instructions.add(trimmed);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instruction, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.bind(position + 1, instructions.get(position));
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private final TextView stepNumber;
        private final TextView stepInstruction;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.step_number);
            stepInstruction = itemView.findViewById(R.id.step_instruction);
        }

        public void bind(int number, String instruction) {
            stepNumber.setText(String.valueOf(number));
            stepInstruction.setText(instruction);
        }
    }
}