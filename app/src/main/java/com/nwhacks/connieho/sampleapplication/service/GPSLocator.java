package com.nwhacks.connieho.sampleapplication.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Owner on 1/13/2018.
 */

public class GPSLocator extends Service implements LocationListener{

    private static final String TAG = "GPS_LOCATOR";
    LocationManager locationManager;
    private Coordinate currentCoordinates;

    public GPSLocator() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        currentCoordinates = new Coordinate();
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
        return currentCoordinates;
    }

    private void updateLocation(Location location){
        Log.d(TAG, "Updating coordinates: " + location.toString());
        currentCoordinates = new Coordinate();
        currentCoordinates.setLatitude(location.getLatitude());
        currentCoordinates.setLongitude(location.getLongitude());
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
