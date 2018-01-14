package com.nwhacks.connieho.sampleapplication.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nwhacks.connieho.sampleapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends ListActivity {
    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];

    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = getListView();
        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1234);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            wifiReciever = new WifiScanReceiver();
            mainWifiObj.startScan();
        }


        //requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 58008);
     /*   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1234);
            Log.v("FUCK", "fuck shit stack sent");
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }else{
            Log.v("FUCK", "Already have fuck shit stack");
            //getScanningResults();
            //do something, permission was previously granted; or legacy device
            wifiReciever = new WifiScanReceiver();
            mainWifiObj.startScan();
        }*/
/*        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();*/

        // listening to single list item on click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String ssid = ((TextView) view).getText().toString();
                connectToWifi(ssid);
                Toast.makeText(MainActivity.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();

            }
        });
    }


    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("FUCK", "Granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.v("FUCK", "Not Granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1234
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            //mWifiListener.getScanningResults();
            Log.v("FUCK", "FUCK THIS SHIT");
        }
    }*/

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            Toast.makeText(MainActivity.this, "got wifi scanner receiver ", Toast.LENGTH_SHORT).show();
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            Log.v("FUCK", "size: " + wifiScanList.size());

            wifis = new String[wifiScanList.size()];
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
            while (it.hasNext()) {
                String ssid = it.next().toString();
                if (!ssid.isEmpty()) {
                    reFiltered.add(ssid);
                }
            }
            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.label, reFiltered));
        }
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
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText(wifiSSID);

        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPassword = pass.getText().toString();
                finallyConnect(checkPassword, wifiSSID);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}