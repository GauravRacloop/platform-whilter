package com.minda.iconnect.spark.config;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.conf.HTTPServer;

import java.net.MalformedURLException;

/**
 * Created by deepakchauhan on 22/07/17.
 */
@ConfArray(LivyConf[].class)
public class LivyConf extends HTTPServer {

    private long batchPurgeInterval;

    public LivyConf() {
        
    }

    public LivyConf(String contextUrl) throws MalformedURLException {
        super(contextUrl);
    }

    public long getBatchPurgeInterval() {
        return batchPurgeInterval;
    }

    public void setBatchPurgeInterval(long batchPurgeInterval) {
        this.batchPurgeInterval = batchPurgeInterval;
    }
}
