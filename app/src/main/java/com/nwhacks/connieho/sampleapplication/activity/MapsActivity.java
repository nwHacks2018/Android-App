package com.nwhacks.connieho.sampleapplication.activity;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.backend.GetClient;
import com.nwhacks.connieho.sampleapplication.backend.PostClient;
import com.nwhacks.connieho.sampleapplication.backend.WifiNetworkList;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(50.0);
        coordinate.setLongitude(120.0);
        List<WifiNetwork> wifiNetworks = Arrays.asList(new WifiNetwork("Shaw Open", "Test", coordinate));
        WifiNetworkList wifiNetworkList = new WifiNetworkList(wifiNetworks);
        // addNetworks(wifiNetworkList);
        // getNetworks();

    }

    public WifiNetworkList getNetworks(){
        WifiNetworkList networks = new WifiNetworkList();
        List<WifiNetwork> networkList = new ArrayList<WifiNetwork>();
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
                    networkList.add(network);
                }
                networks.setNetworks(networkList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return networks;
    }

    public void addNetworks(WifiNetworkList wifiNetworks) {
        for (WifiNetwork wifiNetwork : wifiNetworks.getNetworks()) {
            String urlString = "https://wifinder-294dd.firebaseio.com/Networks.json";
            new PostClient().execute(
                    urlString,
                    wifiNetwork.getSsid(),
                    wifiNetwork.getPassword(),
                    wifiNetwork.getLocation().getLatitude().toString(),
                    wifiNetwork.getLocation().getLongitude().toString());
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
