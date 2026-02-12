package com.example.yumi.presentation.custom;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.yumi.R;


public class ShadowLayout extends FrameLayout {
    private static final int BLUR_LAYERS = 5;

    private Paint[] shadowPaints;
    private RectF shadowRect;
    private float cornerRadius;
    private float shadowRadius;
    private float shadowSpread;
    private int shadowColor;

    public ShadowLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setWillNotDraw(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setClipChildren(false);
        setClipToPadding(false);
        setBackgroundColor(Color.TRANSPARENT);

        // Default values
        shadowColor = R.color.shadow_orange;
        cornerRadius = dpToPx(24);
        shadowRadius = dpToPx(8);
        shadowSpread = dpToPx(15);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(
                    attrs, R.styleable.ShadowLayout, 0, 0);
            try {
                shadowColor = typedArray.getColor(
                        R.styleable.ShadowLayout_shadow_color, shadowColor);
                shadowRadius = typedArray.getDimension(
                        R.styleable.ShadowLayout_shadow_radius, shadowRadius);
                cornerRadius = typedArray.getDimension(
                        R.styleable.ShadowLayout_corner_radius, cornerRadius);
                shadowSpread = typedArray.getDimension(
                        R.styleable.ShadowLayout_shadow_spread, shadowSpread);
            } finally {
                typedArray.recycle();
            }
        }

        shadowRect = new RectF();
        setupPaints();

        int padding = (int) (shadowSpread + shadowRadius);
        setPadding(padding, padding, padding, padding);
    }

    private void setupPaints() {
        shadowPaints = new Paint[BLUR_LAYERS];

        for (int i = 0; i < BLUR_LAYERS; i++) {
            shadowPaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            shadowPaints[i].setStyle(Paint.Style.STROKE);

            float alphaFactor = 1.0f - ((float) i / BLUR_LAYERS);
            int alpha = (int) (80 * alphaFactor);
            shadowPaints[i].setColor(setColorAlpha(shadowColor, alpha));

            float layerBlurRadius = shadowRadius + (shadowSpread * ((float) i / BLUR_LAYERS));
            shadowPaints[i].setStrokeWidth(shadowRadius / 2);
            shadowPaints[i].setMaskFilter(
                    new BlurMaskFilter(layerBlurRadius, BlurMaskFilter.Blur.NORMAL));
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float padding = shadowSpread + shadowRadius;

        for (int i = BLUR_LAYERS - 1; i >= 0; i--) {
            float layerOffset = (shadowSpread * ((float) i / BLUR_LAYERS));

            shadowRect.set(
                    padding - layerOffset,
                    padding - layerOffset,
                    getWidth() - padding + layerOffset,
                    getHeight() - padding + layerOffset
            );

            canvas.drawRoundRect(shadowRect,
                    cornerRadius + layerOffset,
                    cornerRadius + layerOffset,
                    shadowPaints[i]);
        }
    }

    private int setColorAlpha(int color, int alpha) {
        return Color.argb(
                alpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }

    private float dpToPx(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}