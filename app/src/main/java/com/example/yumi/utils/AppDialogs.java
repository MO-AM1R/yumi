package com.example.yumi.utils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.yumi.R;
import com.google.android.material.button.MaterialButton;

public class AppDialogs {

    public interface OnDialogClickListener {
        void onClick();
    }

    public static void showSuccess(
            Context context,
            String title,
            String message,
            String buttonText,
            boolean cancelable,
            OnDialogClickListener onDismiss
    ) {
        Dialog dialog = createBaseDialog(context, cancelable);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);
        dialog.setContentView(view);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        MaterialButton btnPositive = view.findViewById(R.id.btnPositive);

        tvTitle.setText(title);
        tvMessage.setText(message);
        btnPositive.setText(buttonText);

        btnPositive.setOnClickListener(v -> {
            dialog.dismiss();
            if (onDismiss != null) {
                onDismiss.onClick();
            }
        });

        setupDialogWindow(dialog);
        dialog.show();
    }

    public static Dialog showError(
            Context context,
            String title,
            String message,
            String positiveButtonText,
            String negativeButtonText,
            boolean cancelable,
            OnDialogClickListener onPositive,
            OnDialogClickListener onNegative
    ) {
        Dialog dialog = createBaseDialog(context, cancelable);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_error, null);
        dialog.setContentView(view);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        MaterialButton btnPositive = view.findViewById(R.id.btnPositive);
        MaterialButton btnNegative = view.findViewById(R.id.btnNegative);

        tvTitle.setText(title);
        tvMessage.setText(message);
        btnPositive.setText(positiveButtonText);
        btnNegative.setText(negativeButtonText);

        btnPositive.setOnClickListener(v -> {
            dialog.dismiss();
            if (onPositive != null) {
                onPositive.onClick();
            }
        });

        btnNegative.setOnClickListener(v -> {
            dialog.dismiss();
            if (onNegative != null) {
                onNegative.onClick();
            }
        });

        setupDialogWindow(dialog);
        dialog.show();
        return dialog;
    }

    public static Dialog showError(Context context, String title, String message) {
        return showError(context, title, message,  context.getString(R.string.try_again),
                context.getString(R.string.cancel), true, null, null);
    }

    public static Dialog showError(Context context, String title, String message,
                                   OnDialogClickListener onPositive, OnDialogClickListener onNegative) {
        return showError(context, title, message, context.getString(R.string.try_again),
                context.getString(R.string.cancel),  true, onPositive, onNegative);
    }

    public static void showConfirmation(
            Context context,
            String title,
            String message,
            String positiveButtonText,
            String negativeButtonText,
            boolean cancelable,
            OnDialogClickListener onConfirm,
            OnDialogClickListener onCancel
    ) {
        Dialog dialog = createBaseDialog(context, cancelable);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null);
        dialog.setContentView(view);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        MaterialButton btnPositive = view.findViewById(R.id.btnPositive);
        MaterialButton btnNegative = view.findViewById(R.id.btnNegative);

        tvTitle.setText(title);
        tvMessage.setText(message);
        btnPositive.setText(positiveButtonText);
        btnNegative.setText(negativeButtonText);

        btnPositive.setOnClickListener(v -> {
            dialog.dismiss();
            if (onConfirm != null) {
                onConfirm.onClick();
            }
        });

        btnNegative.setOnClickListener(v -> {
            dialog.dismiss();
            if (onCancel != null) {
                onCancel.onClick();
            }
        });

        setupDialogWindow(dialog);
        dialog.show();
    }
    public static void showConfirmation(Context context, String title, String message,
                                        OnDialogClickListener onConfirm, OnDialogClickListener onCancel) {
        showConfirmation(context, title, message,
                context.getString(R.string.confirm),
                context.getString(R.string.cancel), true, onConfirm, onCancel);
    }

    private static Dialog createBaseDialog(Context context, boolean cancelable) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        return dialog;
    }

    private static void setupDialogWindow(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (dialog.getContext().getResources().getDisplayMetrics().widthPixels * 0.88);
            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
}