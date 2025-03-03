package com.whilter.pubsub.serializers;


import com.whilter.pubsub.Serializer;
import com.whilter.pubsub.SerializerFactory;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

public class JavaSerializer implements Serializer<Object>, SerializerFactory {

    @Override
    public byte[] serialize(Object object) {
        return SerializationUtils.serialize(object);
    }

    @Override
    public Object deserialize(byte[] channel) {
        return SerializationUtils.deserialize(channel);
    }

    public Object deserialize(Object model) {
        return model;
    }

    @Override
    public Serializer<?> create() {
        return new JavaSerializer();
    }

    @Override
    public Serializer<?> create(Class<? extends Serializable> serializableClass) {
        return create();
    }
}
