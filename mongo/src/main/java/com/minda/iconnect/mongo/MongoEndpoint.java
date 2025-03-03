package com.minda.iconnect.mongo;

import com.minda.iconnect.platform.core.Endpoint;

/**
 * Created by deepakchauhan on 27/08/17.
 */
public class MongoEndpoint implements Endpoint {

    private MongoConf mongoConf;

    public MongoEndpoint(MongoConf mongoConf) {
        this.mongoConf = mongoConf;
    }

    public MongoConf getMongoConf() {
        return mongoConf;
    }

    @Override
    public String toString() {
        return mongoConf.getID();
    }
}
