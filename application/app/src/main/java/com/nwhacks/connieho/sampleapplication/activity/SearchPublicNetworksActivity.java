package com.nwhacks.connieho.sampleapplication.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.backend.PostClient;
import com.nwhacks.connieho.sampleapplication.backend.WifiNetworkList;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchPublicNetworksActivity extends ListActivity {
    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];

    EditText pass;

    private static final String TAG = "MAP_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        list = getListView();
        mainWifiObj = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        wifiReciever = new WifiScanReceiver();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.v("FUCK", "Didnt have permission, asking user for it.");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                Log.v("FUCK", "Didnt have permission, NOT asking user for it.");
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1234);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Log.v("FUCK", "Already had permission");
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

        // listening to single listView item on click

        Button getSSIDBTN = (Button) findViewById(R.id.getSSIDs);
        getSSIDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainWifiObj.startScan();
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
            getSSIDs(c);
        }


        public void getSSIDs(Context c) {
            Toast.makeText(SearchPublicNetworksActivity.this, "got wifi scanner receiver ", Toast.LENGTH_SHORT).show();
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
            List<WifiNetwork> networkList = new ArrayList<WifiNetwork>();

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

            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.active_list_item, R.id.label, reFiltered));

            WifiNetworkList wifiNetworkList = new WifiNetworkList(networkList);
            addNetworks(wifiNetworkList);
            Toast.makeText(
                    SearchPublicNetworksActivity.this,
                    "Uploaded local public wifi network information to database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    public void addNetworks(WifiNetworkList wifiNetworks) {
        for (WifiNetwork wifiNetwork : wifiNetworks.getNetworks()) {
            String urlString = "https://wifinder-294dd.firebaseio.com/Networks";
            new PostClient().execute(
                    urlString,
                    wifiNetwork.getSsid(),
                    wifiNetwork.getPassword(),
                    wifiNetwork.getLocation().getLatitude().toString(),
                    wifiNetwork.getLocation().getLongitude().toString()
            );
        }
    }

    public void addNetwork(WifiNetwork wifiNetwork) {
        String urlString = "https://wifinder-294dd.firebaseio.com/Networks";
        new PostClient().execute(
                urlString,
                wifiNetwork.getSsid(),
                wifiNetwork.getPassword(),
                ((WiFindApplication) getApplication()).getGlobalVars().getCoordinate().getLatitude().toString(),
                ((WiFindApplication) getApplication()).getGlobalVars().getCoordinate().getLongitude().toString());

    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    10);
        }
    }
}