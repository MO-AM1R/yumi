package com.example.yumi.presentation.custom;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.yumi.R;
import com.example.yumi.data.config.AppConfigurations;
import com.example.yumi.databinding.BottomSheetAddToPlanBinding;
import com.example.yumi.domain.user.model.MealType;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AddToPlanBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetAddToPlanBinding binding;
    private OnConfirmListener onConfirmListener;
    private Date selectedDate;
    private MealType selectedMealType = MealType.BREAKFAST;

    private final MealType[] mealTypes = {
            MealType.BREAKFAST,
            MealType.LUNCH,
            MealType.DINNER,
            MealType.SNACK
    };

    private final SimpleDateFormat dateKeyFormat = new SimpleDateFormat(AppConfigurations.DATE_FORMAT, Locale.getDefault());
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());

    {
        dateKeyFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private long minDateMillis;
    private long maxDateMillis;

    public interface OnConfirmListener {
        void onConfirm(String dateKey, MealType mealType);
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

        initializeDateRange();
        selectedDate = new Date();

        setupDatePicker();
        setupMealTypeDropdown();
        setupButtons();
    }

    private void initializeDateRange() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        minDateMillis = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_MONTH, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        maxDateMillis = calendar.getTimeInMillis();
    }

    private void setupDatePicker() {
        updateDateDisplay();

        binding.dateEditText.setOnClickListener(v -> showDatePicker());
        binding.dateInputLayout.setEndIconOnClickListener(v -> showDatePicker());
    }

    private void updateDateDisplay() {
        binding.dateEditText.setText(displayDateFormat.format(selectedDate));
    }

    private void showDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setStart(minDateMillis)
                .setEnd(maxDateMillis)
                .setOpenAt(selectedDate.getTime())
                .setValidator(new SevenDayValidator(minDateMillis, maxDateMillis));

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(selectedDate.getTime())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);

            selectedDate = calendar.getTime();
            updateDateDisplay();
        });

        datePicker.show(getParentFragmentManager(), "datePicker");
    }

    private void setupMealTypeDropdown() {
        String[] mealTypeNames = new String[mealTypes.length];
        for (int i = 0; i < mealTypes.length; i++) {
            mealTypeNames[i] = getMealTypeName(mealTypes[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                mealTypeNames
        );

        binding.mealTypeDropdown.setAdapter(adapter);
        binding.mealTypeDropdown.setText(getMealTypeName(selectedMealType), false);

        binding.mealTypeDropdown.setOnItemClickListener((parent, view, position, id) ->
                selectedMealType = mealTypes[position]
        );
    }

    private String getMealTypeName(MealType mealType) {
        switch (mealType) {
            case BREAKFAST:
                return getString(R.string.breakfast);
            case LUNCH:
                return getString(R.string.lunch);
            case DINNER:
                return getString(R.string.dinner);
            case SNACK:
                return getString(R.string.snack);
            default:
                return mealType.name();
        }
    }

    private void setupButtons() {
        binding.cancelBtn.setOnClickListener(v -> dismiss());

        binding.confirmBtn.setOnClickListener(v -> {
            if (onConfirmListener != null) {
                String dateKey = dateKeyFormat.format(selectedDate);
                onConfirmListener.onConfirm(dateKey, selectedMealType);
            }
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class SevenDayValidator implements CalendarConstraints.DateValidator {

        private final long minDate;
        private final long maxDate;

        SevenDayValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

        @Override
        public boolean isValid(long date) {
            return date >= minDate && date <= maxDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }

        public static final Creator<SevenDayValidator> CREATOR = new Creator<>() {
            @Override
            public SevenDayValidator createFromParcel(Parcel source) {
                long minDate = source.readLong();
                long maxDate = source.readLong();
                return new SevenDayValidator(minDate, maxDate);
            }

            @Override
            public SevenDayValidator[] newArray(int size) {
                return new SevenDayValidator[size];
            }
        };
    }
}