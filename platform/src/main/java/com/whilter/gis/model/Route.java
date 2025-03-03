package com.whilter.gis.model;

import java.util.List;
import java.util.Objects;

/**
 * Created by mayank on 02/04/19 12:36 PM.
 */
public class Route {
    private List<LatLng> route;
    private long distance;
    private long travelTime;

    public Route() {
    }

    public Route(List<LatLng> route, long distance, long travelTime) {
        this.route = route;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public List<LatLng> getRoute() {
        return route;
    }

    public void setRoute(List<LatLng> route) {
        this.route = route;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route1 = (Route) o;
        return distance == route1.distance &&
                travelTime == route1.travelTime &&
                Objects.equals(route, route1.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, distance, travelTime);
    }

    @Override
    public String toString() {
        return "Route{" +
                "distance=" + distance +
                ", travelTime=" + travelTime +
                ", route=" + route +
                '}';
    }
}
