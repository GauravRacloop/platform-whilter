package com.whilter.pubsub;


import java.io.Serializable;

public interface SerializerFactory {

    Serializer<?> create();

    Serializer<?> create(Class<? extends Serializable> serializableClass);
}
