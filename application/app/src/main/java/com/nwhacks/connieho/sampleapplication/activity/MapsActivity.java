package com.nwhacks.connieho.sampleapplication.activity;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nwhacks.connieho.sampleapplication.R;
import com.nwhacks.connieho.sampleapplication.application.WiFindApplication;
import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.List;

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

        List<WifiNetwork> allSavedNetworks = ((WiFindApplication) getApplication())
                .getGlobalServices()
                .getNetworkRepository().getAllSavedNetworks();

        for (int i = 0; i < allSavedNetworks.size(); i++) {
            if (allSavedNetworks.get(i).getLocation().getLatitude() != null && allSavedNetworks.get(i).getLocation().getLatitude() != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                allSavedNetworks.get(i).getLocation().getLatitude(),
                                allSavedNetworks.get(i).getLocation().getLongitude()))
                        .title(allSavedNetworks.get(i).getSsid()));
            }
        }

        LatLng myLocation = new LatLng(
                ((WiFindApplication) getApplication()).getGlobalVars().getCoordinate().getLatitude(),
                ((WiFindApplication) getApplication()).getGlobalVars().getCoordinate().getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Me")
                .icon(BitmapDescriptorFactory.defaultMarker(263)));
        marker.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
    }
}
