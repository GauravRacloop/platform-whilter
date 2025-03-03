package com.whilter.pubsub.serializers;

import com.whilter.pubsub.Serializer;
import com.whilter.pubsub.SerializerFactory;

import java.io.Serializable;

public class StringSerializer implements Serializer<String>, SerializerFactory {

    @Override
    public byte[] serialize(String object) {
        return object.getBytes();
    }

    @Override
    public String deserialize(byte[] channel) {
        return new String(channel);
    }

    @Override
    public Serializer<?> create() {
        return new StringSerializer();
    }

    @Override
    public Serializer<?> create(Class<? extends Serializable> serializableClass) {
        return create();
    }
}
