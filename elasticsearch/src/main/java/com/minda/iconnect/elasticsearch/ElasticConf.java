package com.minda.iconnect.elasticsearch;

import com.minda.iconnect.platform.conf.Server;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class ElasticConf {

    private String cluster;
    private Server[] servers;

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public Server[] getServers() {
        return servers;
    }
}
