package com.nwhacks.connieho.sampleapplication.datatype;

public class WifiNetwork {

    private String ssid;

    private String password;

    private Coordinate location;

    public WifiNetwork() {
    }

    public WifiNetwork(String ssid, String password, Coordinate location) {
        this.ssid = ssid;
        this.password = password;
        this.location = location;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }
}
