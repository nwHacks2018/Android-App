package com.nwhacks.connieho.sampleapplication.application;

import com.nwhacks.connieho.sampleapplication.service.NetworkRepository;

public class GlobalServices {

    private NetworkRepository networkRepository;

    public NetworkRepository getNetworkRepository() {
        if(networkRepository == null) {
            networkRepository = new NetworkRepository();
        }
        return networkRepository;
    }

}
