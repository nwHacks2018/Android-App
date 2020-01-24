package com.nwhacks.luminescence.wifind.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nwhacks.luminescence.wifind.R;
import com.nwhacks.luminescence.wifind.service.GPSLocator;

public class MainActivity extends Activity {

    private static final String TAG = "MAP_ACTIVITY";
    public GPSLocator gpsLocator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent autoconnectorIntent = new Intent(this, Autoconnector.class);
//        startService(autoconnectorIntent);
//        Intent autoRetrieverIntent = new Intent(this, AutoRetriever.class);
//        startService(autoRetrieverIntent);

        Button launcher1Btn = findViewById(R.id.launcher1Btn);
        launcher1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        ScanActivity.class);
                startActivity(myIntent);
            }
        });

        Button launcher2Btn = findViewById(R.id.launcher2Btn);
        launcher2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        SearchPublicNetworksActivity.class);
                startActivity(myIntent);
            }
        });

        Button launcher3Btn = findViewById(R.id.launcher3Btn);
        launcher3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        RetrieveActivity.class);
                startActivity(myIntent);
            }
        });

        Button launcher4Btn = findViewById(R.id.launcher4Btn);
        launcher4Btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent myIntent = new Intent(MainActivity.this,
                                                        SavedNetworksActivity.class);
                                                startActivity(myIntent);
                                            }
                                        });

        Button launcher5Btn = findViewById(R.id.launcher5Btn);
        launcher5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        MapsActivity.class);
                startActivity(myIntent);
            }
        });

        initializeGPSLocator();

    }


    @Override
    public void onResume(){
        super.onResume();

        final TextView currentSSIDTextView = (TextView) findViewById(R.id.currentSSIDMain);
        String currentSSID = getCurrentSsid(this);

      // Drawable drawable = this.getResources().getDrawable(R.drawable.rounded_corner);


        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(15);

        if (currentSSID == null){
            drawable.setStroke(3, getResources().getColor(R.color.disconnectedTextColor));
            drawable.setColor(getResources().getColor(R.color.disconnectedBackgroundColor));
            currentSSIDTextView.setTextColor(getResources().getColor(R.color.disconnectedTextColor));
            currentSSIDTextView.setText("Not currently connected to a network");

        } else {
            drawable.setStroke(3, getResources().getColor(R.color.connectedTextColor));
            drawable.setColor(getResources().getColor(R.color.connectedBackgroundColor));
            currentSSIDTextView.setTextColor(getResources().getColor(R.color.connectedTextColor));

            currentSSIDTextView.setText("Currently connected to: " + currentSSID);
        }
        currentSSIDTextView.setBackgroundDrawable(drawable);

    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    private void initializeGPSLocator() {
        Log.d(TAG, "Creating GPS locator");
        gpsLocator = new GPSLocator();
        checkLocationPermissions();
        Intent serviceIntent = new Intent(this, GPSLocator.class);
        startService(serviceIntent);
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    10);
        }
    }
}
