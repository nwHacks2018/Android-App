package com.nwhacks.connieho.sampleapplication.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;

/**
 * Created by Owner on 1/13/2018.
 */

public class GPSLocator extends Service implements LocationListener{

    private static final String TAG = "GPS_LOCATOR";
    LocationManager locationManager;

    public GPSLocator() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Coordinate location = new Coordinate();
        location.setLongitude(0.0);
        location.setLatitude(0.0);
        ((WiFindApplication) getApplication()).getGlobalVars().setCoordinate(location);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        registerForUpdates();
    }

    private void registerForUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permissions not granted");
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    public Coordinate GetCurrentLocation(){
        return ((WiFindApplication) getApplication()).getGlobalVars().getCoordinate();
    }

    private void updateLocation(Location location){
        Log.d(TAG, "Updating coordinates: " + location.toString());
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(location.getLatitude());
        coordinate.setLongitude(location.getLongitude());
        ((WiFindApplication) getApplication()).getGlobalVars().setCoordinate(coordinate);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        updateLocation(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
