package com.minda.iconnect.gis.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.minda.iconnect.platform.exception.ApplicationException;
import com.minda.iconnect.platform.gis.GISService;
import com.minda.iconnect.platform.gis.model.Route;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mayank on 30/10/17.
 */
public class GoogleGISService implements GISService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleGISService.class);

    private final GeoApiContext geoApiContext;

    public GoogleGISService(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public Route getRoute(com.minda.iconnect.platform.gis.model.LatLng source, com.minda.iconnect.platform.gis.model.LatLng target) {
        try {
            LatLng sourceLatLng = new LatLng(source.getLatitude(), source.getLongitude());
            LatLng targetLatLng = new LatLng(target.getLatitude(), target.getLongitude());

            DirectionsResult result = DirectionsApi.newRequest(geoApiContext).mode(TravelMode.DRIVING)
                    .origin(sourceLatLng)
                    .destination(targetLatLng)
                    .departureTime(new DateTime())
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .units(Unit.METRIC).alternatives(false).await();

            DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(geoApiContext).mode(TravelMode.DRIVING)
                    .origins(sourceLatLng)
                    .destinations(targetLatLng)
                    .units(Unit.METRIC).await();

            Route route = prepareRoute(result, distanceMatrix);
            return route;
        } catch (ApiException | InterruptedException | IOException e) {
            LOGGER.error("Error finding route", e);
            throw new ApplicationException("Route Not found betwee " + source + " and " + target, e);
        }
    }

    @Override
    public Route getRoute(String source, String target) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext).mode(TravelMode.DRIVING)
                    .origin(source)
                    .destination(target)
                    .departureTime(new DateTime())
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .units(Unit.METRIC).await();

            DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(geoApiContext).mode(TravelMode.DRIVING)
                    .origins(source)
                    .destinations(target)
                    .units(Unit.METRIC).await();

            Route route = prepareRoute(result, distanceMatrix);

            return route;
        } catch (ApiException | InterruptedException | IOException e) {
            LOGGER.error("Error finding route", e);
            throw new ApplicationException("Route Not found betwee " + source + " and " + target, e);
        }
    }

    @Override
    public com.minda.iconnect.platform.gis.model.LatLng getGeoCodes(String address) {
        try {
            GeocodingResult[] result = GeocodingApi.newRequest(geoApiContext).address(address).await();
            if (result.length == 0) {
                LOGGER.error("Geo Coordinates not found for address : " + address);
                throw new ApplicationException("Address Not found: " + address);
            }

            return new com.minda.iconnect.platform.gis.model.LatLng(result[0].geometry.location.lat, result[0].geometry.location.lng);
        } catch (ApiException | InterruptedException | IOException e) {
            LOGGER.error("Geo Coordinates not found for address : " + address, e);
            throw new ApplicationException("Geo Points Not found: " + address, e);
        }
    }

    @Override
    public String getAddress(com.minda.iconnect.platform.gis.model.LatLng point) {
        try {
            GeocodingResult[] result = GeocodingApi.newRequest(geoApiContext).latlng(new LatLng(point.getLatitude(), point.getLongitude())).await();
            if (result.length == 0) {
                LOGGER.error("Address not found for geo coordinates: " + point);
                throw new ApplicationException("Address not found for geo coordinates: " + point);
            }

            return result[0].formattedAddress;
        } catch (ApiException | InterruptedException | IOException e) {
            LOGGER.error("Error finding Address", e);
            throw new ApplicationException("Address not found for: " + point, e);
        }
    }

    @Override
    public void start() throws Exception {
        //Do Nothing
    }

    @Override
    public void shutdown() {
        //Do Nothing
    }

    @Override
    public boolean autoStart() {
        return false;
    }

    private Route prepareRoute(DirectionsResult result, DistanceMatrix distanceMatrix) throws JsonProcessingException {
        Route route = new Route();
        List<com.minda.iconnect.platform.gis.model.LatLng> locations = new LinkedList<>();
        for (DirectionsLeg leg : result.routes[0].legs) {
            for (DirectionsStep step : leg.steps) {
                List<LatLng> latLngs = step.polyline.decodePath();
                for (LatLng latLng : latLngs) {
                    locations.add(new com.minda.iconnect.platform.gis.model.LatLng(latLng.lat, latLng.lng));
                }
            }
        }

        route.setRoute(locations);

        DistanceMatrixRow[] rows = distanceMatrix.rows;
        if (rows != null &&  rows.length > 0) {
            DistanceMatrixElement[] elements = rows[0].elements;
            if (elements != null && elements.length > 0) {
                route.setDistance(elements[0].distance.inMeters);
                route.setTravelTime(elements[0].duration.inSeconds);
            }
        }
        return route;
    }

}
