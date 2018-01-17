package com.nwhacks.connieho.sampleapplication.service;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Autoconnector extends Service {

    private static String TAG = AutoRetriever.class.getSimpleName();

    WifiManager mainWifiObj;

    WifiScanReceiver wifiReciever;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mainWifiObj = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        wifiReciever = new WifiScanReceiver();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Automatically checking if wifi is connected.");
                if (getCurrentSsid(getApplicationContext()) == null ||
                        getCurrentSsid(getApplicationContext()).isEmpty()) {
                    attemptAutoConnection();
                }
            }
        }).start();

    }

    public void attemptAutoConnection() {

        List<String> availableNetworkNames = wifiReciever.getSSIDs(getApplicationContext());
        List<WifiNetwork> savedNetworks = ((WiFindApplication) getApplication())
                .getGlobalServices()
                .getNetworkRepository()
                .getAllSavedNetworks();

        String ssid = null;
        String pass = null;
        for(String i : availableNetworkNames) {
            if(getCurrentSsid(getApplicationContext()) != null) {
                break;
            }
            for(WifiNetwork j : savedNetworks) {
                if(j.getSsid().equals(i) && j.getPassword() != null && !j.getPassword().isEmpty()) {
                    Log.d(TAG, "Attempting to connect to network " + ssid);
                    finallyConnect(pass, ssid);
                    break;
                }
            }
        }

        if(getCurrentSsid(getApplicationContext()) != null) {
            Toast.makeText(getApplicationContext(), "Connected to wifi SSID: " + ssid, Toast.LENGTH_SHORT).show();
        }
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

    private void finallyConnect(String networkPass, String networkSSID) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        // remember id
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        mainWifiObj.reconnect();

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"\"" + networkSSID + "\"\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        mainWifiObj.addNetwork(conf);

        WifiNetwork wifi = new WifiNetwork();
        wifi.setSsid(networkSSID);
        wifi.setPassword(networkPass);
        wifi.setLocation(((WiFindApplication) getApplication()).getGlobalVars().getCoordinate());
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
        }

        public ArrayList<String> getSSIDs(Context c) {

            // Toast.makeText(ScanActivity.this, "got wifi scanner receiver ", Toast.LENGTH_SHORT).show();
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            Log.v("FUCK", "size: " + wifiScanList.size());

            String[] wifis = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {
                Log.v("FUCK", "ssid: " + wifiScanList.get(i).SSID);
                wifis[i] = ((wifiScanList.get(i)).toString());
            }
            String filtered[] = new String[wifiScanList.size()];
            int counter = 0;
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");

                filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength

                counter++;

            }

            Set<String> set = new HashSet<String>();
            for (int i = 0; i < wifiScanList.size(); i++) {
                set.add(filtered[i]);
            }
            Iterator it = set.iterator();
            ArrayList<String> reFiltered = new ArrayList<String>();
            List<WifiNetwork> networkList = new ArrayList<WifiNetwork>();

            if (((WiFindApplication) getApplication()).getGlobalVars().getCoordinate() == null) {
                Coordinate coordinate = new Coordinate();
                coordinate.setLongitude(49.0);
                coordinate.setLongitude(-123.0);
                ((WiFindApplication) getApplication()).getGlobalVars().setCoordinate(coordinate);
            }

            while (it.hasNext()) {
                String ssid = it.next().toString();
                if (!ssid.isEmpty()) {
                    reFiltered.add(ssid);
                    WifiNetwork network = new WifiNetwork(
                            ssid,
                            "",
                            ((WiFindApplication) getApplication()).getGlobalVars().getCoordinate());
                    networkList.add(network);
                }
            }

            return reFiltered;
        }
    }
}
