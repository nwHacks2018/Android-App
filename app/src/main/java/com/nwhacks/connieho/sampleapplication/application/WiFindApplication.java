package com.nwhacks.connieho.sampleapplication.application;

import android.app.Application;

public class WiFindApplication extends Application {

    private GlobalServices globalServices;

    public GlobalServices getGlobalServices() {
        if(globalServices == null) {
            globalServices = new GlobalServices();
        }
        return globalServices;
    }

}
