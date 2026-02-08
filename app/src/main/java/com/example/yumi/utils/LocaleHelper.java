package com.example.yumi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.Nullable;

import static com.example.yumi.data.config.SharedPreferencesKeysConfig.KEY_LANGUAGE;
import static com.example.yumi.data.config.SharedPreferencesKeysConfig.PREF_NAME;

import java.util.Locale;
import java.util.Objects;


public abstract class LocaleHelper {
    public static final String LANG_ENGLISH = "en";
    public static final String LANG_ARABIC = "ar";

    @Nullable
    public static String getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, null);
    }

    public static void saveLanguage(Context context, String lang) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, lang)
                .apply();
    }

    public static Context attach(Context context) {
        String savedLang = getSavedLanguage(context);

        if (savedLang == null) {
            return context;
        }

        return updateLocale(context, new Locale(savedLang));
    }

    public static Context setLocale(Context context, String language) {
        saveLanguage(context, language);
        return updateLocale(context, new Locale(language));
    }

    private static Context updateLocale(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        config.setLocale(locale);
        config.setLayoutDirection(locale);

        return context.createConfigurationContext(config);
    }

    public static boolean isSameLanguage(Context context, String selectedLang) {
        String savedLang = getSavedLanguage(context);

        return
                Objects.requireNonNullElseGet(
                                savedLang,
                                () -> Locale.getDefault().getLanguage())
                        .equalsIgnoreCase(selectedLang);

    }

    public static String getCurrentLanguage(Context context) {
        String savedLang = getSavedLanguage(context);
        return savedLang != null ? savedLang : Locale.getDefault().getLanguage();
    }

    public static boolean isArabic(Context context) {
        return LANG_ARABIC.equals(getCurrentLanguage(context));
    }
}