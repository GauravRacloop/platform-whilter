package com.whilter.gis.util;

import com.whilter.gis.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mayank on 02/04/19 12:17 PM.
 */
public class GISCircleUtil {

    public static final double RADIUS_OF_EARTH_METERS = 63_71_000D;

    public static List<LatLng> circleCircumference(LatLng centerPoint, int radius, double points) {
        double d2r = Math.PI / 180;   // degrees to radians
        double r2d = 180 / Math.PI;   // radians to degrees

        // find the raidus in lat/lon
        double rlat = (radius / RADIUS_OF_EARTH_METERS) * r2d;
        double rlng = rlat / Math.cos(centerPoint.getLatitude() * d2r);

        List<LatLng> extp = new ArrayList<>();

        for (double i = 0; i < points; i = i + 1) {
            double theta = Math.PI * (i / (points / 2));
            double ey = centerPoint.getLongitude() + (rlng * Math.sin(theta)); // center a + radius x * cos(theta)
            double ex = centerPoint.getLatitude() + (rlat * Math.cos(theta)); // center b + radius y * sin(theta)
            extp.add(new LatLng(ex, ey));
        }
        return extp;
    }
}
