package com.whilter.pubsub;

import java.io.Serializable;

public class StringSerializerFactory implements SerializerFactory {

    @Override
    public Serializer<?> create() {
        return new StringSerializer();
    }

    @Override
    public Serializer<?> create(Class<? extends Serializable> serializableClass) {
        return create();
    }
}
