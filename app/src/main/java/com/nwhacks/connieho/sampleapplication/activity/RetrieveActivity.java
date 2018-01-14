package com.nwhacks.connieho.sampleapplication.activity;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.backend.GetClient;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RetrieveActivity extends ListActivity {

    ListView listView;
    String wifis[];

    private static final String TAG = RetrieveActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);

        listView = getListView();

        List<WifiNetwork> list = getNetworks();

        ((WiFindApplication) getApplication())
                .getGlobalServices()
                .getNetworkRepository()
                .addPublicNetworks(list);

        wifis = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Log.v(TAG, "Retrieving SSID from server: " + list.get(i).getSsid());
            wifis[i] = list.get(i).getSsid();
        }

        listView.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.list_item,
                R.id.label,
                wifis
        ));

        Toast.makeText(this, "Networks saved.", Toast.LENGTH_LONG).show();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public List<WifiNetwork> getNetworks(){
        List<WifiNetwork> networkList = new ArrayList<>();
        String urlString = "https://wifinder-294dd.firebaseio.com/Networks.json";
        AsyncTask<String, Void, String> getRequest = new GetClient().execute(urlString);
        try {
            String result = getRequest.get();
            try {
                JSONObject object = new JSONObject(result);
                Iterator<String> iterator = object.keys();
                while (iterator.hasNext()) {
                    JSONObject obj = object.getJSONObject(iterator.next());
                    Coordinate coordinate = new Coordinate();
                    coordinate.setLatitude(Double.parseDouble(obj.getJSONObject("Coordinate").getString("Latitude")));
                    coordinate.setLongitude(Double.parseDouble(obj.getJSONObject("Coordinate").getString("Longitude")));
                    WifiNetwork network = new WifiNetwork(
                            obj.getString("SSID"),
                            obj.getString("Password"),
                            coordinate);
                    Log.d("Network: ", network.getSsid());
                    networkList.add(network);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return networkList;
    }

}