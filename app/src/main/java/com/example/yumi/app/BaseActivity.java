package com.example.yumi.app;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yumi.utils.LocaleHelper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }
}