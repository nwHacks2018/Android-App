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
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
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
import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.backend.PostClient;
import com.nwhacks.connieho.sampleapplication.backend.WifiNetworkList;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ScanActivity extends ListActivity {
    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];

    EditText pass;

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String ssid = ((TextView) view.findViewById(R.id.label)).getText().toString();
                connectToWifi(ssid);
                Toast.makeText(ScanActivity.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();

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
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            getSSIDs(c);
        }

    public void getSSIDs(Context c){
//        Toast.makeText(ScanActivity.this, "got wifi scanner receiver ", Toast.LENGTH_SHORT).show();
        List<ScanResult> wifiScanList = mainWifiObj.getScanResults();

        wifis = new String[wifiScanList.size()];
        for (int i = 0; i < wifiScanList.size(); i++) {
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

        if (((WiFindApplication) getApplication()).getGlobalVars().getCoordinate() == null){
            Coordinate coordinate =  new Coordinate();
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

        final TextView currentSSIDTextView = (TextView) findViewById(R.id.currentSSID);
        String currentSSID = getCurrentSsid(c);

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


        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.active_list_item, R.id.label, reFiltered));
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
        addNetwork(wifi);
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");

        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);
        textSSID.setText(wifiSSID);



        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);

        String password = getPassword(wifiSSID);
        pass.setText(password);
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

    private String getPassword(String wifiSSID){

        List<WifiNetwork> allSavedNetworks = ((WiFindApplication) getApplication())
                .getGlobalServices()
                .getNetworkRepository().getAllSavedNetworks();

        for (int i = 0; i < allSavedNetworks.size(); i++) {
            Log.d("ssid", allSavedNetworks.get(i).getSsid() + " COMPARE TO " + wifiSSID);
            if (allSavedNetworks.get(i).getSsid().equals(wifiSSID)) {
                return allSavedNetworks.get(i).getPassword();
            }
        }
        return "";
    }
}