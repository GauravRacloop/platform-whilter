package com.whilter.gis;

import com.whilter.core.Service;
import com.whilter.gis.model.LatLng;
import com.whilter.gis.model.LatLngBounds;
import com.whilter.gis.model.Route;
import com.whilter.gis.util.GISCircleUtil;
import com.whilter.gis.util.GisGeofenceUtil;
import com.whilter.gis.util.GisPolygonUtil;

import java.util.List;

/**
 * Created by mayank on 30/10/17.
 */
public interface GISService extends Service {

    double EARTH_RADIUS_METERS = 6371000;

    Route getRoute(String source, String target);

    Route getRoute(LatLng source, LatLng target);

    LatLng getGeoCodes(String address);

    String getAddress(LatLng point);

    default int calculateDistance(LatLng l1, LatLng l2) {
        return GisGeofenceUtil.calculateDistance(l1, l2);
    }

    default int calculateDistance(List<LatLng> locations) {
        return GisGeofenceUtil.calculateDistance(locations);
    }

    default List<LatLng> decodeCoordinates(String encodedCoordinates) {
        return GisGeofenceUtil.decodeCoordinates(encodedCoordinates);
    }

    default String encodeCoordinates(List<LatLng> coordinates) {
        return GisGeofenceUtil.encodeCoordinates(coordinates);
    }

    default List<LatLng> getCircleCoordinates(LatLng center, int radius, int numberOfPoints) {
        return GISCircleUtil.circleCircumference(center, radius, numberOfPoints);
    }

    default boolean isInsideGeofence(LatLng geofenceCenter, int radius, LatLng observablePoint) {
        return calculateDistance(geofenceCenter, observablePoint) < radius;
    }

    default boolean isPointInsidePolygon(List<LatLng> polygon, LatLng point) {
        return GisPolygonUtil.containsLocation(polygon, point, true);
    }

    default List<LatLng> bufferPolyLine(List<LatLng> polyLine, int bufferInMeters) {
        return GisPolygonUtil.bufferPolyLine(polyLine, bufferInMeters);
    }

    default LatLng getCenterOfPolyGon(List<LatLng> polygon) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        return bounds.getCenter();
    }

    default boolean validateLatitudeLongitude(Double lat, Double lng) {
        return (lat != null && lng != null)
                && (lat >= -90D && lat <= 90D)
                && (lng >= -180D && lng <= 180D);
    }

    default int resolveBufferDistance(List<LatLng> polyLine) {
        return GisPolygonUtil.resolveBufferDistance(polyLine);
    }
}
