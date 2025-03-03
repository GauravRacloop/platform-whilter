package com.minda.iconnect.mongo;

import com.minda.iconnect.platform.core.Service;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by deepakchauhan on 27/08/17.
 */
public interface MongoService extends Service {

    MongoClient syncClient();

    com.mongodb.rx.client.MongoClient rxClient();

    MongoDatabase syncDB();

    com.mongodb.rx.client.MongoDatabase rxDB();

}
