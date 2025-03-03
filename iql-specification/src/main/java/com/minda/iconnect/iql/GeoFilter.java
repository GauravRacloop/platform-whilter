package com.minda.iconnect.iql;

public class GeoFilter {

    private String field;
    private double longitude;
    private double latitude;
    private double radius;

    public GeoFilter(String field, double longitude, double latitude, double radius) {
        this.field = field;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getRadius() {
        return radius;
    }

    public String getField() {
        return field;
    }
}
