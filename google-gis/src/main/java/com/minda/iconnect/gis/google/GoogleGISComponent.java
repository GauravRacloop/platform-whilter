package com.minda.iconnect.gis.google;

import com.google.maps.GeoApiContext;
import com.minda.iconnect.gis.google.endpoint.GoogleGISEndpoint;
import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.gis.GISComponent;
import com.minda.iconnect.platform.gis.GISService;

import java.util.concurrent.TimeUnit;

/**
 * Created by mayank on 30/10/17.
 */
public class GoogleGISComponent extends AbstractComponent<GoogleGISEndpoint, GISService> implements GISComponent<GoogleGISEndpoint, GISService>{

    @Override
    protected GISService doGet(GoogleGISEndpoint endpoint) {
        return new GoogleGISService(new GeoApiContext.Builder()
                .apiKey(endpoint.getGisConf().getApiKey())
                .connectTimeout(endpoint.getGisConf().getConnectTimeout(), TimeUnit.SECONDS)
                .maxRetries(endpoint.getGisConf().getMaxRetries()).build());
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(GoogleGISEndpoint endpoint) {
        return endpoint.getGisConf().getApiKey();
    }
}
