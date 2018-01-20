package com.nwhacks.connieho.sampleapplication.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.backend.BackendClient;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AutoRetriever extends Service {

    private static String TAG = AutoRetriever.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Automatically retrieving networks from server.");
                List<WifiNetwork> list = BackendClient.getRemotelySavedNetworks();

                ((WiFindApplication) getApplication())
                        .getGlobalServices()
                        .getNetworkRepository()
                        .addPublicNetworks(list);
            }
        }).start();

    }
}
