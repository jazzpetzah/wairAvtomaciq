package com.wearezeta.auto.common.wire_actors.models;

public class LocationInfo {
    private float lon;
    private float lat;
    private String address;
    private int zoom;

    public LocationInfo(float lon, float lat, String address, int zoom) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.zoom = zoom;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public String getAddress() {
        return address;
    }

    public int getZoom() {
        return zoom;
    }
}
