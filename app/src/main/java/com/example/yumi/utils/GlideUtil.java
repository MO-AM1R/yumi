package com.example.yumi.utils;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.yumi.R;


public abstract class GlideUtil {
    public static void getImage(Context context, ImageView imageView, String imageUrl){
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.network_error)
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);
    }
}