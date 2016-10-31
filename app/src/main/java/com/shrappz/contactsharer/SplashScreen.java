package com.shrappz.contactsharer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.shrappz.contactsharer.utils.PrefConnector;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PrefConnector.readBoolean(SplashScreen.this, getString(R.string.key_first_open))) {
                    startActivity(new Intent(SplashScreen.this, DemoSlider.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else {
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        }, 3 * 1000);
    }
}
