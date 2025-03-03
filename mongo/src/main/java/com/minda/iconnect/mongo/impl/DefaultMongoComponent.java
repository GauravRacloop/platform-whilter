package com.minda.iconnect.mongo.impl;

import com.minda.iconnect.mongo.MongoComponent;
import com.minda.iconnect.mongo.MongoEndpoint;
import com.minda.iconnect.mongo.MongoService;
import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

/**
 * Created by deepakchauhan on 27/08/17.
 */
public class DefaultMongoComponent extends AbstractComponent<MongoEndpoint, MongoService> implements MongoComponent {

    @Override
    protected MongoService doGet(MongoEndpoint endpoint) {
        String db = endpoint.getMongoConf().getAuthDb() == null ? endpoint.getMongoConf().getDb() : endpoint.getMongoConf().getAuthDb();

        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.readPreference(ReadPreference.primary());
        builder.writeConcern(WriteConcern.ACKNOWLEDGED);

        MongoClientURI mongoClientURI = new MongoClientURI(
                endpoint.getMongoConf().toConnectionString(db, null),builder);


        return new DefaultMongoService(mongoClientURI, endpoint.getMongoConf().getDb());
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(MongoEndpoint endpoint) {
        return endpoint.getMongoConf().getID();
    }
}
