package com.nwhacks.luminescence.wifind.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import android.util.Log;

import com.nwhacks.luminescence.wifind.application.WiFindApplication;
import com.nwhacks.luminescence.wifind.backend.BackendClient;
import com.nwhacks.luminescence.wifind.datatype.WifiNetwork;

import java.util.List;

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
