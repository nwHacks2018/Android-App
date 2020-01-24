package com.nwhacks.luminescence.wifind;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.nwhacks.luminescence.wifind.activity.MainActivity;
import com.nwhacks.luminescence.wifind.application.DeviceServices;
import com.nwhacks.luminescence.wifind.application.Dialogs;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private static int DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide both the navigation bar and the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        waitUntilLocationEnabled(1000);

    }

    private void waitUntilLocationEnabled(long delayMillis) {
        final Activity thisActivity = this;

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if (DeviceServices.deniedByUser(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Dialogs.exitWithoutLocationPermissions(thisActivity);
            }
            else if (DeviceServices.hasPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            else {
                DeviceServices.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                DeviceServices.requireLocationEnabled(thisActivity);
                waitUntilLocationEnabled(delayMillis);
            }

        }, delayMillis);
    }

}