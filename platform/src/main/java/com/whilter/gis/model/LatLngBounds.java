package com.whilter.gis.model;

/**
 * Created by mayank on 02/04/19 12:04 PM.
 */
public class LatLngBounds {

    private final LatLng northeast;
    private final LatLng southwest;

    /**
     * Constructs a new object given southwest and northwest points.
     * @param southwest
     * @param northeast
     */
    public LatLngBounds(LatLng southwest, LatLng northeast) {
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public LatLng getSouthwest() {
        return southwest;
    }

    public LatLng getNortheast() {
        return northeast;
    }

    /**
     * Determines whether the given point is contained within the lat/lng's bounds.
     * @param point
     * @return
     */
    public boolean contains(LatLng point) {
        return this.includesLat(point.getLatitude()) && this.includesLng(point.getLongitude());
    }

    /**
     * Returns a new object which includes the given point.
     * @param point
     * @return
     */
    public LatLngBounds including(LatLng point) {
        double swLat = Math.min(this.southwest.getLatitude(), point.getLatitude());
        double neLat = Math.max(this.northeast.getLatitude(), point.getLatitude());
        double neLng = this.northeast.getLongitude();
        double swLng = this.southwest.getLongitude();
        double ptLng = point.getLongitude();
        if (!this.includesLng(ptLng)) {
            if (swLngMod(swLng, ptLng) < neLngMod(neLng, ptLng)) {
                swLng = ptLng;
            } else {
                neLng = ptLng;
            }
        }

        return new LatLngBounds(new LatLng(swLat, swLng), new LatLng(neLat, neLng));
    }

    /**
     * Returns the center of the lat/lng bounds.
     * @return
     */
    public LatLng getCenter() {
        double midLat = (this.southwest.getLatitude() + this.northeast.getLatitude()) / 2.0D;
        double neLng = this.northeast.getLongitude();
        double swLng = this.southwest.getLongitude();
        double midLng;
        if (swLng <= neLng) {
            midLng = (neLng + swLng) / 2.0D;
        } else {
            midLng = (neLng + 360.0D + swLng) / 2.0D;
        }

        return new LatLng(midLat, midLng);
    }

    private static double swLngMod(double swLng, double ptLng) {
        return (swLng - ptLng + 360.0D) % 360.0D;
    }

    private static double neLngMod(double neLng, double ptLng) {
        return (ptLng - neLng + 360.0D) % 360.0D;
    }

    private boolean includesLat(double lat) {
        return this.southwest.getLatitude() <= lat && lat <= this.northeast.getLatitude();
    }

    private boolean includesLng(double lng) {
        return this.southwest.getLongitude() <= this.northeast.getLongitude() ?
                this.southwest.getLongitude() <= lng && lng <= this.northeast.getLongitude() :
                this.southwest.getLongitude() <= lng || lng <= this.northeast.getLongitude();
    }

    /**
     * Builder class for {@link LatLngBounds}.
     */
    public static class Builder {

        private double northeastLat = 0;
        private double northeastLng = 0;
        private double southwestLat = 0;
        private double southwestLng = 0;

        /**
         * Includes this point for building of the bounds. The bounds will be extended in a minimum way
         * to include this point.
         * @param point
         * @return builder object
         */
        public Builder include(LatLng point) {
            if (northeastLat == 0) {
                northeastLat = point.getLatitude();
                northeastLng = point.getLongitude();
                southwestLat = point.getLatitude();
                southwestLng = point.getLongitude();
            }
            if (point.getLatitude() > northeastLat) {
                northeastLat = point.getLatitude();
            } else if (point.getLatitude() < southwestLat) {
                southwestLat = point.getLatitude();
            }
            if (point.getLongitude() > northeastLng) {
                northeastLng = point.getLongitude();
            } else if (point.getLongitude() < southwestLng) {
                southwestLng = point.getLongitude();
            }
            return this;
        }

        /**
         * Constructs a new {@link LatLngBounds} from current boundaries.
         * @return
         */
        public LatLngBounds build() {
            LatLng sw = new LatLng(southwestLat, southwestLng);
            LatLng ne = new LatLng(northeastLat, northeastLng);
            return new LatLngBounds(sw, ne);
        }
    }
}
