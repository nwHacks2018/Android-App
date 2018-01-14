package com.nwhacks.connieho.sampleapplication.service;

import com.nwhacks.connieho.sampleapplication.datatype.WifiNetwork;

import java.util.ArrayList;
import java.util.List;

public class NetworkRepository {

    private List<WifiNetwork> savedNetworks;

    public NetworkRepository() {
        this.savedNetworks = new ArrayList<>();
    }

    // Retrieves the list of saved networks from the device.
    public List<WifiNetwork> getSavedNetworks() {
        return savedNetworks;
    }

    // Adds a set of networks to the networks saved in the device.
    // Conflicting existing SSIDs are overwritten.
    // Non-conflicting existing SSIDs are not modified.
    public void addNetworks(List<WifiNetwork> newNetworks) {

        // Remove duplicated SSIDs
        for(WifiNetwork i : newNetworks) {
            for(WifiNetwork j : savedNetworks) {
                if(i.getSsid().equals(j.getSsid())) {
                    savedNetworks.remove(j);
                }
            }
        }

        savedNetworks.addAll(newNetworks);
    }

}
