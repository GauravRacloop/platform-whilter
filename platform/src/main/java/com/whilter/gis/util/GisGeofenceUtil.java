package com.whilter.gis.util;

import com.whilter.gis.GISService;
import com.whilter.gis.model.LatLng;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mayank on 02/04/19 12:21 PM.
 */
public class GisGeofenceUtil {
    public static List<LatLng> decodeCoordinates(String coordinates) {
        List<LatLng> decodedList = Stream.of(coordinates.split("\\|"))
                .map(val -> {
                    String[] split = val.split(":");
                    if (split.length == 2) {
                        double latitude = Double.parseDouble(split[0]);
                        double longitude = Double.parseDouble(split[1]);
                        return new LatLng(latitude, longitude);
                    } else {
                        throw new IllegalArgumentException("Invalid Coordinate: " + val);
                    }
                }).collect(Collectors.toList());

        if (decodedList.size() > 1 && !decodedList.get(0).equals(decodedList.get(decodedList.size() - 1))) {
            decodedList.add(decodedList.get(0));
        }

        return decodedList;
    }

    public static String encodeCoordinates(List<LatLng> geoPointModels) {
        return geoPointModels.stream()
                .map(val -> val.getLatitude() + ":" + val.getLongitude())
                .collect(Collectors.joining("|"));
    }

    /**
     *
     * @param locationL1
     * @param locationL2
     * @return Distance between two geo-points in Meters
     * Calculates the distance between two points using Haversine's Distance Formula
     */
    public static int calculateDistance(LatLng locationL1, LatLng locationL2) {

        double latDistance = Math.toRadians(locationL1.getLatitude() - locationL2.getLatitude());
        double lngDistance = Math.toRadians(locationL1.getLongitude() - locationL2.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(locationL1.getLatitude())) * Math.cos(Math.toRadians(locationL2.getLatitude()))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(GISService.EARTH_RADIUS_METERS * c));
    }

    /**
     *
     * @param locations
     * @return Calculated distance between all the points
     * Calculates the distance between two points using Haversine's Distance Formula
     */
    public static int calculateDistance(List<LatLng> locations) {
        int distance = 0;
        LatLng previous = null;
        for (LatLng location : locations) {
            if (previous != null) {
                distance = distance + calculateDistance(previous, location);
            }
            previous = location;
        }
        return distance;
    }

}
