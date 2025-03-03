package com.whilter.gis;

import com.whilter.conf.GISConf;
import com.whilter.core.Endpoint;

/**
 * Created by mayank on 30/10/17.
 */
public class GISEndpoint implements Endpoint {

    private GISConf gisConf;

    public GISEndpoint(GISConf gisConf) {
        this.gisConf = gisConf;
    }

    public GISConf getGisConf() {
        return gisConf;
    }
}
