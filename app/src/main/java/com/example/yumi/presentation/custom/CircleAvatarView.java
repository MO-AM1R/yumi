package com.example.yumi.presentation.custom;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
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
        radius = dpToPx(DEFAULT_RADIUS);
        text = "";
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        foregroundColor = DEFAULT_FOREGROUND_COLOR;
        isBold = true;

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
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

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

        canvas.drawCircle(centerX, centerY, drawRadius, backgroundPaint);

        if (text != null && !text.isEmpty()) {
            float actualTextSize = textSize > 0 ? textSize : drawRadius * DEFAULT_TEXT_SIZE_RATIO * 2;
            textPaint.setTextSize(actualTextSize);

            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textY = centerY - (fontMetrics.ascent + fontMetrics.descent) / 2;

            canvas.drawText(text, centerX, textY, textPaint);
        }
    }

    public void setRadius(int radiusDp) {
        this.radius = dpToPx(radiusDp);
        requestLayout();
        invalidate();
    }

    public void setText(@Nullable String text) {
        this.text = text != null ? text : "";
        invalidate();
    }

    public void setTextSize(float sizeSp) {
        this.textSize = spToPx(sizeSp);
        invalidate();
    }

    public int getRadius() {
        return radius;
    }

    public String getText() {
        return text;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private float spToPx(float sp) {
        return sp * getResources().getDisplayMetrics().scaledDensity;
    }
}