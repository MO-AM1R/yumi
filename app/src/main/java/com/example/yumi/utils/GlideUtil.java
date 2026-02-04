package com.example.yumi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.yumi.R;


public abstract class GlideUtil {
    public static void getImage(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.network_error)
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);
    }

    public static void getImageWithGeneratedBackground(
            Context context,
            ImageView imageView,
            CardView backgroundView,
            String imageUrl
    ) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.network_error)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(
                            @NonNull Bitmap bitmap,
                            @Nullable Transition<? super Bitmap> transition
                    ) {
                        imageView.setImageBitmap(bitmap);

                        Palette.from(bitmap).generate(palette -> {
                            int defaultColor =
                                    ContextCompat.getColor(context, R.color.background);

                            int dominantColor = defaultColor;
                            if (palette != null)
                                dominantColor =
                                        palette.getDominantColor(defaultColor);

                            backgroundView.setCardBackgroundColor(dominantColor);
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        imageView.setImageDrawable(placeholder);
                    }
                });
    }

}