package com.nwhacks.connieho.sampleapplication.service;

import android.util.Log;

import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.ArrayList;
import java.util.List;

public class NetworkRepository {

    private static String TAG = NetworkRepository.class.getSimpleName();

    private List<WifiNetwork> savedLocalPrivateNetworks;

    private List<WifiNetwork> savedPublicNetworks;

    public NetworkRepository() {
        this.savedLocalPrivateNetworks = new ArrayList<>();
        this.savedPublicNetworks = new ArrayList<>();
    }

    // Used only for sending local public networks to the server
    public List<WifiNetwork> getSavedPublicNetworks() {
        return savedPublicNetworks;
    }

    // Used only for local connection to available networks
    public List<WifiNetwork> getAllSavedNetworks() {
        List<WifiNetwork> networks = new ArrayList<>();
        networks.addAll(savedLocalPrivateNetworks);
        networks.addAll(savedPublicNetworks);
        return networks;
    }

    public void addLocalPrivateNetworks(List<WifiNetwork> newNetworks) {
        combineWifiNetworkLists(savedLocalPrivateNetworks, newNetworks);
    }

    // Adds a set of networks to the networks saved in the device.
    // Conflicting existing SSIDs are overwritten.
    // Non-conflicting existing SSIDs are not modified.
    public void addPublicNetworks(List<WifiNetwork> newNetworks) {
        combineWifiNetworkLists(savedPublicNetworks, newNetworks);
    }

    // All duplicate SSIDs are removed from the first list before combining
    private void combineWifiNetworkLists(List<WifiNetwork> list1, List<WifiNetwork> list2) {

        List<WifiNetwork> removalList = new ArrayList<>();

        for(WifiNetwork i : list2) {
            Log.d(TAG, "Updating network " + i.getSsid());
            for(WifiNetwork j : list1) {
                if(i.getSsid().equals(j.getSsid())) {
                    removalList.add(j);
                }
            }
        }

        list1.removeAll(removalList);
        list1.addAll(list2);
    }

}
