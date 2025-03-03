package com.minda.iconnect.elasticsearch;

import com.whilter.core.Endpoint;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class ElasticEndpoint implements Endpoint {

    private ElasticConf elasticConf;

    public ElasticEndpoint(ElasticConf elasticConf) {
        this.elasticConf = elasticConf;
    }

    public ElasticConf getElasticConf() {
        return elasticConf;
    }
}
