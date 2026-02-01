package com.example.yumi.common.customviews;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.example.yumi.R;


public class WormDotIndicator extends LinearLayout {
    private int count;
    private int marginPx;
    private int inactiveWidthPx;
    private int activeWidthPx;
    private int heightPx;
    private int inactiveColor;
    private int activeColor;
    private int animationDuration;


    private int index;

    public WormDotIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public WormDotIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WormDotIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        index = 0;
        count = 3;
        marginPx = (int) (4 * metrics.density);
        heightPx = (int) (8 * metrics.density);
        inactiveWidthPx = (int) (8 * metrics.density);
        activeWidthPx = (int) (24 * metrics.density);
        animationDuration = 300;

        //TODO: set my colors from colors
        inactiveColor = Color.parseColor("#80CA2E55");
        activeColor = Color.parseColor("#CA2E55");

        if (attrs != null) {
            TypedArray attrArray = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.DotIndicator, 0, 0);
            try {
                count = attrArray.getInt(R.styleable.DotIndicator_count, count);
                marginPx = attrArray.getDimensionPixelSize(R.styleable.DotIndicator_dot_margin, marginPx);
                heightPx = attrArray.getDimensionPixelSize(R.styleable.DotIndicator_common_height, heightPx);
                inactiveWidthPx = attrArray.getDimensionPixelSize(R.styleable.DotIndicator_inactive_width, inactiveWidthPx);
                activeWidthPx = attrArray.getDimensionPixelSize(R.styleable.DotIndicator_active_width, activeWidthPx);
                inactiveColor = attrArray.getColor(R.styleable.DotIndicator_inactive_color, inactiveColor);
                activeColor = attrArray.getColor(R.styleable.DotIndicator_active_color, activeColor);
                animationDuration = attrArray.getInt(R.styleable.DotIndicator_animation_duration, animationDuration);
            } finally {
                attrArray.recycle();
            }
        }

        createDots();
    }

    private void createDots() {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(inactiveWidthPx, heightPx);
            params.setMargins(marginPx, 0, marginPx, 0);
            dot.setLayoutParams(params);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(heightPx / 2f);
            drawable.setColor(inactiveColor);

            dot.setBackground(drawable);
            addView(dot);
        }

        selectDot(0, false);
    }

    public void selectDot(int position, boolean animate) {
        if (position < 0 || position >= getChildCount()) return;

        if (animate) {
            ChangeBounds transition = new ChangeBounds();
            transition.setDuration(animationDuration);
            transition.setInterpolator(new FastOutSlowInInterpolator());
            TransitionManager.beginDelayedTransition(this, transition);
        }

        clearOldActiveDot();
        index = position;
        setNewActiveDot();
    }

    private void setNewActiveDot() {
        View dot = getChildAt(index);
        ViewGroup.LayoutParams params = dot.getLayoutParams();
        GradientDrawable bg = (GradientDrawable) dot.getBackground();
        params.width = activeWidthPx;
        bg.setColor(activeColor);
        dot.setLayoutParams(params);
    }

    private void clearOldActiveDot() {
        View dot = getChildAt(index);
        ViewGroup.LayoutParams params = dot.getLayoutParams();
        GradientDrawable bg = (GradientDrawable) dot.getBackground();
        params.width = inactiveWidthPx;
        bg.setColor(inactiveColor);
        dot.setLayoutParams(params);
    }

    public int getIndex(){
        return index;
    }

    public void setCount(int count) {
        this.count = count;
        createDots();
    }
}