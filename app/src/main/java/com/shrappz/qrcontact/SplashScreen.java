package com.shrappz.qrcontact;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.shrappz.qrcontact.utils.PrefConnector;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        // Used Handler postdelayed to delay splash screen and continue to preceeding activity based on shared pref
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PrefConnector.readBoolean(SplashScreen.this, getString(R.string.key_first_open))) {
                    startActivity(new Intent(SplashScreen.this, DemoSlider.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }, 3 * 1000);
    }
}
