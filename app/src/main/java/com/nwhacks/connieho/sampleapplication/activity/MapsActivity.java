package com.nwhacks.connieho.sampleapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.datatype.Coordinate;
import com.nwhacks.connieho.sampleapplication.service.GPSLocator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MAP_ACTIVITY";
    private GoogleMap mMap;
    private GPSLocator gpsLocator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initializeGPSLocator();
    }

    private void initializeGPSLocator() {
        Log.d(TAG, "Creating GPS locator");
        gpsLocator = new GPSLocator();
        checkLocationPermissions();
        Intent serviceIntent = new Intent(this, GPSLocator.class);
        startService(serviceIntent);
        try {
            queryLocation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        10);
        }
    }

    private void queryLocation() throws InterruptedException {
        for (int i = 0; i < 500; i++){
            Coordinate location = gpsLocator.GetCurrentLocation();
            if (location != null) {
                Toast.makeText(this, gpsLocator.GetCurrentLocation().toString(), Toast.LENGTH_LONG).show();
                Thread.sleep((long)3000);
            }
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
