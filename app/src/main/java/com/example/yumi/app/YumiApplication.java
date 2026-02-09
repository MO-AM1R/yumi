package com.example.yumi.app;
import android.app.Application;
import android.content.Context;

import com.example.yumi.data.database.AppDatabase;
import com.example.yumi.data.firebase.FirebaseModule;
import com.example.yumi.utils.LocaleHelper;
import com.example.yumi.utils.ThemeHelper;


public class YumiApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.attach(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseModule.initialize(this);
        ThemeHelper.applySavedTheme(this);
        AppDatabase.getInstance(getApplicationContext());
    }
}