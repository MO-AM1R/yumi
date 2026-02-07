package com.example.yumi.presentation.custom;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.yumi.R;
import com.example.yumi.databinding.BottomSheetAddToPlanBinding;
import com.example.yumi.domain.user.model.MealType;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddToPlanBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetAddToPlanBinding binding;
    private OnConfirmListener onConfirmListener;
    private Date selectedDate ;
    private MealType selectedMealType = MealType.BREAKFAST;
    private final MealType[] mealTypes = {MealType.BREAKFAST, MealType.LUNCH, MealType.DINNER, MealType.SNACK};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnConfirmListener {
        void onConfirm(String date, MealType mealType);
    }

    public static AddToPlanBottomSheet newInstance() {
        return new AddToPlanBottomSheet();
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.onConfirmListener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.ThemeOverlay_Yumi_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAddToPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedDate = new Date();
        setupDatePicker();
        setupMealTypeDropdown();
        setupButtons();
    }

    private void setupDatePicker() {
        binding.dateEditText.setText(dateFormat.format(selectedDate));
        binding.dateEditText.setOnClickListener(v -> showDatePicker());
        binding.dateInputLayout.setEndIconOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(dateFormat.getCalendar().getTimeInMillis())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = new Date(selection);
            binding.dateEditText.setText(dateFormat.format(selectedDate));
        });

        datePicker.show(getParentFragmentManager(), "datePicker");
    }

    private void setupMealTypeDropdown() {
        ArrayAdapter<MealType> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                mealTypes
        );
        binding.mealTypeDropdown.setAdapter(adapter);
        binding.mealTypeDropdown.setText(MealType.fromMealType(selectedMealType), false);

        binding.mealTypeDropdown.setOnItemClickListener((parent, view, position, id) ->
                selectedMealType = mealTypes[position]);
    }

    private void setupButtons() {
        binding.cancelBtn.setOnClickListener(v -> dismiss());

        binding.confirmBtn.setOnClickListener(v -> {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(dateFormat.format(selectedDate), selectedMealType);
            }
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}