package com.example.yumi.utils;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import static com.example.yumi.data.config.SharedPreferencesKeysConfig.PREF_NAME;
import static com.example.yumi.data.config.SharedPreferencesKeysConfig.KEY_LANGUAGE;

import com.example.yumi.R;

import java.util.Locale;

public class LocaleHelper {
    public static String getSavedLanguage(Context context) {
        return context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANGUAGE,
                        Locale.getDefault().getLanguage());
    }

    public static void saveLanguage(Context context, String lang) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, lang)
                .apply();
    }

    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static boolean isSameLanguage(Context context, String selectedLang) {
        String currentLangCode = getSavedLanguage(context);

        String localizedLang = context.getString(R.string.ar);
        if (currentLangCode.equalsIgnoreCase("en"))
            localizedLang = context.getString(R.string.en);


        Log.d("TAG", "currentLangCode: " + currentLangCode);
        Log.d("TAG", "localizedLang: " + currentLangCode);
        Log.d("TAG", "Selected: " + selectedLang);
        return localizedLang.equalsIgnoreCase(selectedLang);
    }
}
