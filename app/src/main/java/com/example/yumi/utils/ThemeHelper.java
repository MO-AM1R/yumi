package com.example.yumi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import static com.example.yumi.data.config.SharedPreferencesKeysConfig.PREF_NAME;
import static com.example.yumi.data.config.SharedPreferencesKeysConfig.KEY_DARK_MODE;


public abstract class ThemeHelper {
    public static void applySavedTheme(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (!sharedPreferences.contains(KEY_DARK_MODE)) {
            Log.d("TAG", "System");
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            );
            return;
        }

        boolean isDark = sharedPreferences.getBoolean(KEY_DARK_MODE, false);

        AppCompatDelegate.setDefaultNightMode(
                isDark
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    public static void saveTheme(Context context, boolean isDark) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_DARK_MODE, isDark)
                .apply();
    }

    @Nullable
    public static Boolean getSavedTheme(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (!sharedPreferences.contains(KEY_DARK_MODE)) {
            return null;
        }
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }
}
