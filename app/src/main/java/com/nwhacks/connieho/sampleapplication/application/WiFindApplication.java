package com.nwhacks.connieho.sampleapplication.application;

import android.app.Application;

public class WiFindApplication extends Application {

    private GlobalServices globalServices;

    private GlobalVars globalVars;

    public GlobalServices getGlobalServices() {
        if(globalServices == null) {
            globalServices = new GlobalServices();
        }
        return globalServices;
    }

    public GlobalVars getGlobalVars() {
        if(globalVars == null) {
            globalVars = new GlobalVars();
        }
        return globalVars;
    }

}
