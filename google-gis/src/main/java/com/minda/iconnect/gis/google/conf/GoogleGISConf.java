package com.minda.iconnect.gis.google.conf;

import com.minda.iconnect.platform.conf.GISConf;

/**
 * Created by mayank on 30/10/17.
 */
public class GoogleGISConf extends GISConf {

    private int connectTimeout;
    private int maxRetries;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
