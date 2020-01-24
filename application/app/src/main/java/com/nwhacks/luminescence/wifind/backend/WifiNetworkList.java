package com.nwhacks.luminescence.wifind.backend;

import com.nwhacks.luminescence.wifind.datatype.WifiNetwork;

import java.util.List;

public class WifiNetworkList {

    private List<WifiNetwork> networks;

    public WifiNetworkList() {
    }

    public WifiNetworkList(List<WifiNetwork> networks) {
        this.networks = networks;
    }

    public List<WifiNetwork> getNetworks() {
        return networks;
    }

    public void setNetworks(List<WifiNetwork> networks) {
        this.networks = networks;
    }

}
