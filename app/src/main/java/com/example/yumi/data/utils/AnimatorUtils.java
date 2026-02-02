package com.example.yumi.data.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public abstract class AnimatorUtils {
    public static ObjectAnimator getFadeInAnimation(View view, int duration, float... values) {
        ObjectAnimator fadeIn;

        if (values.length == 0)
            fadeIn = ObjectAnimator.ofFloat(
                    view, "alpha", 0f, 1f);
        else
            fadeIn = ObjectAnimator.ofFloat(
                    view, "alpha", values);

        fadeIn.setDuration(duration);
        return fadeIn;
    }

    public static ObjectAnimator getFadeInAnimation(View view, float... values) {
        if (values.length == 0)
            return ObjectAnimator.ofFloat(
                    view, "alpha", 0f, 1f);

        return ObjectAnimator.ofFloat(
                view, "alpha", values);
    }

    public static AnimatorSet getScaleAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                view, "scaleX", 0f, 1f);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                view, "scaleY", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);

        return animatorSet;
    }

    public static ObjectAnimator getTranslateY(View view, float start, float end) {
        return ObjectAnimator.ofFloat(
                view, "translationY", start, end);
    }
}
