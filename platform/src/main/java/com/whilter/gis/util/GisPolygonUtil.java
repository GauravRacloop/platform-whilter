package com.whilter.gis.util;

import com.whilter.gis.GISService;
import com.whilter.gis.model.LatLng;
import com.whilter.gis.model.PolyEdge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.whilter.gis.util.GisGeofenceUtil.calculateDistance;
import static java.lang.Math.*;

/**
 * Created by mayank on 02/04/19 12:27 PM.
 */
public class GisPolygonUtil {
    private static final double D_TO_R = Math.PI / 180;   // degrees to radians
    private static final double R_TO_D = 180 / Math.PI;   // radians to degrees

    public static boolean containsLocation(List<LatLng> polygon, LatLng point, boolean geodesic) {
        final int size = polygon.size();
        if (size == 0) {
            return false;
        }
        double lat3 = Math.toRadians(point.getLatitude());
        double lng3 = Math.toRadians(point.getLongitude());

        LatLng prev = polygon.get(size - 1);
        double lat1 = Math.toRadians(prev.getLatitude());
        double lng1 = Math.toRadians(prev.getLongitude());
        int nIntersect = 0;

        for (LatLng point2 : polygon) {
            double dLng3 = wrap(lng3 - lng1, -PI, PI);
            // Special case: point equal to vertex is inside.
            if (lat3 == lat1 && dLng3 == 0) {
                return true;
            }
            double lat2 = Math.toRadians(point2.getLatitude());
            double lng2 = Math.toRadians(point2.getLongitude());
            // Offset longitudes by -lng1.
            if (intersects(lat1, lat2, wrap(lng2 - lng1, -PI, PI), lat3, dLng3, geodesic)) {
                ++nIntersect;
            }
            lat1 = lat2;
            lng1 = lng2;
        }
        return (nIntersect & 1) != 0;
    }

    public static List<LatLng> bufferPolyLine(List<LatLng> route, int bufferInMeters) {
        if (route == null || route.size() < 4) {
            throw new IllegalArgumentException("There must be atleast 4 legs in the path");
        }

        ArrayList<LatLng> up = new ArrayList<>();
        ArrayList<LatLng> down = new ArrayList<>();

        Double previousAngle = null;
        for (int counter = 1; counter < route.size(); counter++) {
            PolyEdge currentEdge = new PolyEdge(route.get(counter - 1), route.get(counter));
            previousAngle = bufferPoint(currentEdge, previousAngle, bufferInMeters, up, down);
        }
        Collections.reverse(down);

        List<LatLng> finalPath = Stream.concat(up.stream(), down.stream()).collect(Collectors.toList());
        finalPath.add(finalPath.get(0));
        return finalPath;
    }

    private static Double bufferPoint(PolyEdge polyEdge, Double previousAngle, int distanceInMeters, ArrayList<LatLng> up, ArrayList<LatLng> down) {

        double currentAzimuth = Math.abs(calculateAngleFromNorthPole(polyEdge.getStart(), polyEdge.getEnd()));

        double upAngle;
        if (previousAngle == null) {
            upAngle = normalizeAngle(currentAzimuth + 90D);
        } else {
            double totalChangedAngle = normalizeAngle(360D - ((180D - previousAngle) + currentAzimuth));
            if (totalChangedAngle < 0D) {
                double halfOfChangedAngle = normalizeAngle(Math.abs(totalChangedAngle) / 2D);
                upAngle = normalizeAngle(previousAngle + halfOfChangedAngle);
            } else {
                double halfOfChangedAngle = normalizeAngle(totalChangedAngle / 2D);
                upAngle = normalizeAngle(currentAzimuth + halfOfChangedAngle);
            }

        }

        up.add(getPointAtAngle(polyEdge.getStart(), upAngle, distanceInMeters));

        down.add(getPointAtAngle(polyEdge.getEnd(), normalizeAngle(upAngle + 180D), distanceInMeters));
        return currentAzimuth;
    }

    private static double normalizeAngle(double angle) {
        return angle > 360 ? angle % 360D : angle;
    }

    private static LatLng getPointAtAngle(LatLng start, double angle, double distanceInMeters) {
        double rlatStart = (distanceInMeters / GISService.EARTH_RADIUS_METERS) * R_TO_D;
        double rlngStart = rlatStart / Math.cos(start.getLatitude() * D_TO_R);

        double angleRadians = Math.PI * (angle / (180D));
        double latitude = start.getLatitude() + (rlatStart * Math.cos(angleRadians));
        double longitude = start.getLongitude() + (rlngStart * Math.sin(angleRadians));
        return new LatLng(latitude, longitude);
    }

    /**
     * Calculates Azimuth.
     * Azimuth is angle of a line from north pole (normal)
     * @param start
     * @param end
     * @return
     */
    private static double calculateAngleFromNorthPole(LatLng start, LatLng end) {
        double startLat = Math.toRadians(start.getLatitude());
        double startLong = Math.toRadians(start.getLongitude());
        double endLat = Math.toRadians(end.getLatitude());
        double endLong = Math.toRadians(end.getLongitude());

        double dLong = endLong - startLong;

        double dPhi = Math.log(Math.tan((endLat / 2.0) +
                (Math.PI / 4.0)) / Math.tan((startLat / 2.0) + (Math.PI / 4.0)));

        if (Math.abs(dLong) > Math.PI) {
            if (dLong > 0.0) {
                dLong = -(2.0 * Math.PI - dLong);
            } else {
                dLong = (2.0 * Math.PI + dLong);
            }
        }
        double bearing = (Math.toDegrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
        return bearing;
    }

    @SuppressWarnings("SameParameterValue")
    private static double wrap(double n, double min, double max) {
        return (n >= min && n < max) ? n : (mod(n - min, max - min) + min);
    }

    private static boolean intersects(double lat1, double lat2, double lng2,
                                      double lat3, double lng3, boolean geodesic) {
        // Both ends on the same side of lng3.
        if ((lng3 >= 0 && lng3 >= lng2) || (lng3 < 0 && lng3 < lng2)) {
            return false;
        }
        // Point is South Pole.
        if (lat3 <= -PI / 2) {
            return false;
        }
        // Any segment end is a pole.
        if (lat1 <= -PI / 2 || lat2 <= -PI / 2 || lat1 >= PI / 2 || lat2 >= PI / 2) {
            return false;
        }
        if (lng2 <= -PI) {
            return false;
        }
        double linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2;
        // Northern hemisphere and point under lat-lng line.
        if (lat1 >= 0 && lat2 >= 0 && lat3 < linearLat) {
            return false;
        }
        // Southern hemisphere and point above lat-lng line.
        if (lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat) {
            return true;
        }
        // North Pole.
        if (lat3 >= PI / 2) {
            return true;
        }
        // Compare lat3 with latitude on the GC/Rhumb segment corresponding to lng3.
        // Compare through a strictly-increasing function (tan() or mercator()) as convenient.
        return geodesic ?
                tan(lat3) >= tanLatGC(lat1, lat2, lng2, lng3) :
                mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3);
    }

    private static double tanLatGC(double lat1, double lat2, double lng2, double lng3) {
        return (tan(lat1) * sin(lng2 - lng3) + tan(lat2) * sin(lng3)) / sin(lng2);
    }

    private static double mercatorLatRhumb(double lat1, double lat2, double lng2, double lng3) {
        return (mercator(lat1) * (lng2 - lng3) + mercator(lat2) * lng3) / lng2;
    }

    private static double mercator(double lat) {
        return log(tan(lat * 0.5 + PI / 4));
    }

    private static double mod(double x, double m) {
        return ((x % m) + m) % m;
    }

    public static int resolveBufferDistance(List<LatLng> polyLine) {
        Map<Integer, Integer> counter = new HashMap<>();
        for (int i = 2; i < polyLine.size() / 2; i++) {
            int dis = (calculateDistance(polyLine.get(i), polyLine.get(polyLine.size() - (i +1))) + 1) / 2;
            Integer count = counter.get(dis);
            if (count == null) {
                count = 0;
            }
            count = count + 1;
            counter.put(dis, count);
        }

        Map.Entry<Integer, Integer> entry = counter.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElse(null);
        return entry == null ? 0 : entry.getKey();
    }
}
