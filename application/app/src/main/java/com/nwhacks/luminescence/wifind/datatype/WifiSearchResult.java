package com.nwhacks.luminescence.wifind.datatype;

public class WifiSearchResult {

    private String ssid;

    private WifiAccessPermission accessPermission;

    public String getSsid() {
        return ssid;
    }

    public WifiSearchResult() {
    }

    public WifiSearchResult(String ssid, WifiAccessPermission accessPermission) {
        this.ssid = ssid;
        this.accessPermission = accessPermission;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public WifiAccessPermission getAccessPermission() {
        return accessPermission;
    }

    public void setAccessPermissions(WifiAccessPermission accessPermission) {
        this.accessPermission = accessPermission;
    }
}
