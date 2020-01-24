package com.nwhacks.luminescence.wifind.backend;

import android.os.AsyncTask;
import android.util.Log;

import com.nwhacks.luminescence.wifind.datatype.Coordinate;
import com.nwhacks.luminescence.wifind.datatype.WifiNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BackendClient {

    public static List<WifiNetwork> getRemotelySavedNetworks() {
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
