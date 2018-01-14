package com.nwhacks.connieho.sampleapplication.backend;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by connieho on 2018-01-13.
 */

public class PostClient extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            String urlString = params[0];
            String ssid = params[1];
            String password = params[2];
            String geoLat = params[3];
            String geoLong = params[4];

            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("SSID", ssid);
            jsonParam.put("Password", password);
            jsonParam.put("Coordinate", new JSONObject()
                    .put("Latitude", geoLat)
                    .put("Longitude", geoLong));

            wr.writeBytes(jsonParam.toString());

            wr.flush();
            wr.close();

            return Util.inputStreamToString(conn.getInputStream()).toString();

        } catch (Exception e) {
            Log.d("exception", e.getMessage());
            return e.getMessage();
        }
    }
}


