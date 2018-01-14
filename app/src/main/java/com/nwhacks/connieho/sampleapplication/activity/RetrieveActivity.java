package com.nwhacks.connieho.sampleapplication.activity;


import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.backend.BackendClient;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.List;

public class RetrieveActivity extends ListActivity {

    ListView listView;
    String wifis[];

    private static final String TAG = RetrieveActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);

        listView = getListView();

        List<WifiNetwork> list = BackendClient.getRemotelySavedNetworks();

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

}