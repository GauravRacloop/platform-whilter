package com.minda.iconnect.gis.google.endpoint;

import com.minda.iconnect.gis.google.conf.GoogleGISConf;
import com.minda.iconnect.platform.gis.GISEndpoint;

/**
 * Created by mayank on 30/10/17.
 */
public class GoogleGISEndpoint extends GISEndpoint {

    private final GoogleGISConf gisConf;

    public GoogleGISEndpoint(GoogleGISConf gisConf) {
        super(gisConf);
        this.gisConf = gisConf;
    }

    @Override
    public GoogleGISConf getGisConf() {
        return gisConf;
    }

}
