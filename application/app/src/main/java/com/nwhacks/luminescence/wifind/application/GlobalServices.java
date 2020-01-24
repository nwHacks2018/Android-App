package com.nwhacks.luminescence.wifind.application;

import com.nwhacks.luminescence.wifind.service.NetworkRepository;

public class GlobalServices {

    private NetworkRepository networkRepository;

    public NetworkRepository getNetworkRepository() {
        if(networkRepository == null) {
            networkRepository = new NetworkRepository();
        }
        return networkRepository;
    }

}
