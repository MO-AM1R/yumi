package com.example.yumi.presentation.custom;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yumi.R;

public class CircleAvatarView extends View {

    private Paint backgroundPaint;
    private Paint textPaint;

    private int radius;
    private String text;
    private int backgroundColor;
    private int foregroundColor;
    private float textSize;
    private boolean isBold;

    // Default values
    private static final int DEFAULT_RADIUS = 48; // dp
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#F5A623");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final float DEFAULT_TEXT_SIZE_RATIO = 0.4f;

    public CircleAvatarView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        // Default values
        radius = dpToPx(DEFAULT_RADIUS);
        text = "";
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        foregroundColor = DEFAULT_FOREGROUND_COLOR;
        isBold = true;

        // Read attributes
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleAvatarView);

            try {
                radius = typedArray.getDimensionPixelSize(
                        R.styleable.CircleAvatarView_cav_radius,
                        dpToPx(DEFAULT_RADIUS)
                );
                text = typedArray.getString(R.styleable.CircleAvatarView_cav_text);
                if (text == null) text = "";

                backgroundColor = typedArray.getColor(
                        R.styleable.CircleAvatarView_cav_backgroundColor,
                        DEFAULT_BACKGROUND_COLOR
                );
                foregroundColor = typedArray.getColor(
                        R.styleable.CircleAvatarView_cav_foregroundColor,
                        DEFAULT_FOREGROUND_COLOR
                );
                textSize = typedArray.getDimension(
                        R.styleable.CircleAvatarView_cav_textSize,
                        0
                );
                isBold = typedArray.getBoolean(
                        R.styleable.CircleAvatarView_cav_textBold,
                        true
                );
            } finally {
                typedArray.recycle();
            }
        }

        setupPaints();
    }

    private void setupPaints() {
        // Background paint
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        // Text paint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(foregroundColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (isBold) {
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int diameter = radius * 2;
        int width = resolveSize(diameter, widthMeasureSpec);
        int height = resolveSize(diameter, heightMeasureSpec);

        // Keep it square
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int drawRadius = Math.min(width, height) / 2;

        // Draw background circle
        canvas.drawCircle(centerX, centerY, drawRadius, backgroundPaint);

        // Draw text
        if (text != null && !text.isEmpty()) {
            // Calculate text size if not set
            float actualTextSize = textSize > 0 ? textSize : drawRadius * DEFAULT_TEXT_SIZE_RATIO * 2;
            textPaint.setTextSize(actualTextSize);

            // Center text vertically
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textY = centerY - (fontMetrics.ascent + fontMetrics.descent) / 2;

            canvas.drawText(text, centerX, textY, textPaint);
        }
    }

    // ==================== Public Setters ====================

    public void setRadius(int radiusDp) {
        this.radius = dpToPx(radiusDp);
        requestLayout();
        invalidate();
    }

    public void setRadiusPx(int radiusPx) {
        this.radius = radiusPx;
        requestLayout();
        invalidate();
    }

    public void setText(@Nullable String text) {
        this.text = text != null ? text : "";
        invalidate();
    }

    public void setInitials(@NonNull String fullName) {
        this.text = generateInitials(fullName);
        invalidate();
    }

    public void setAvatarBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }

    public void setForegroundColor(@ColorInt int color) {
        this.foregroundColor = color;
        textPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float sizeSp) {
        this.textSize = spToPx(sizeSp);
        invalidate();
    }

    public void setTextBold(boolean bold) {
        this.isBold = bold;
        textPaint.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        invalidate();
    }

    // ==================== Public Getters ====================

    public int getRadius() {
        return radius;
    }

    public String getText() {
        return text;
    }

    public int getAvatarBackgroundColor() {
        return backgroundColor;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    // ==================== Utility Methods ====================

    private String generateInitials(@NonNull String fullName) {
        if (fullName.trim().isEmpty()) {
            return "";
        }

        String[] parts = fullName.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        } else {
            String first = parts[0].isEmpty() ? "" : parts[0].substring(0, 1);
            String last = parts[parts.length - 1].isEmpty() ? "" : parts[parts.length - 1].substring(0, 1);
            return (first + last).toUpperCase();
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private float spToPx(float sp) {
        return sp * getResources().getDisplayMetrics().scaledDensity;
    }
}