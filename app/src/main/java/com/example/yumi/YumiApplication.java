package com.example.yumi;
import android.app.Application;
import com.example.yumi.data.firebase.FirebaseModule;


public class YumiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseModule.initialize(this);
    }
}