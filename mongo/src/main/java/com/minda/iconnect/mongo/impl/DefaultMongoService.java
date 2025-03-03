package com.minda.iconnect.mongo.impl;

import com.minda.iconnect.mongo.MongoService;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.rx.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Created by deepakchauhan on 27/08/17.
 */
public class DefaultMongoService extends AbstractService implements MongoService {

    private MongoClient syncClient;
    private com.mongodb.rx.client.MongoClient rxClient;
    private String db;

    private MongoClientURI uri;

    private CodecRegistry codecRegistry;

    public DefaultMongoService(MongoClientURI uri, String db) {
        this.uri = uri;
        this.db = db;
    }

    @Override
    public MongoClient syncClient() {
        return syncClient;
    }

    @Override
    public com.mongodb.rx.client.MongoClient rxClient() {
        return rxClient;
    }

    @Override
    public MongoDatabase syncDB() {
        return syncClient.getDatabase(db).withCodecRegistry(codecRegistry);
    }

    @Override
    public com.mongodb.rx.client.MongoDatabase rxDB() {
        return rxClient.getDatabase(db).withCodecRegistry(codecRegistry);
    }

    @Override
    protected void doStart() throws IOException {
        codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        syncClient = new MongoClient(uri);
        rxClient = MongoClients.create(new ConnectionString(uri.toString()));
    }

    @Override
    protected void doShutdown() {
        syncClient.close();
        rxClient.close();
    }

    @Override
    public boolean autoStart() {
        return true;
    }
}
