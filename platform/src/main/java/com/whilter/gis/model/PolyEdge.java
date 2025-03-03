package com.whilter.gis.model;

import java.util.Objects;

/**
 * Created by mayank on 02/04/19 12:05 PM.
 */
public class PolyEdge {
    private LatLng start;
    private LatLng end;

    public PolyEdge() {
    }

    public PolyEdge(LatLng start, LatLng end) {
        this.start = start;
        this.end = end;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getEnd() {
        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolyEdge polyEdge = (PolyEdge) o;
        return Objects.equals(start, polyEdge.start) &&
                Objects.equals(end, polyEdge.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "PolyEdge{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
