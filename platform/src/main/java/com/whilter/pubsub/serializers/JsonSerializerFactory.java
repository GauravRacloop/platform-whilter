package com.whilter.pubsub.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whilter.pubsub.Serializer;
import com.whilter.pubsub.SerializerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by mayank on 01/08/19 12:50 PM.
 */
public class JsonSerializerFactory implements SerializerFactory {

    @Override
    public Serializer<?> create() {
        return new JsonSerializer(null);
    }

    @Override
    public JsonSerializer create(Class<? extends Serializable> serializableClass) {
        return new JsonSerializer(serializableClass);
    }

    public static void main(String[] args) {
        JsonSerializer<HashMap> stringJsonSerializer = new JsonSerializer<>(HashMap.class);
        System.out.println(stringJsonSerializer.deserialize(stringJsonSerializer.serialize(new HashMap())));
    }

    public static class JsonSerializer<T extends Serializable> implements Serializer<T> {
        private final ThreadLocal<ObjectMapper> objectMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);
        private final Class<? extends Serializable> serializableClass;

        public JsonSerializer(Class<T> serializableClass) {
            if (serializableClass == null) {
                throw new IllegalArgumentException("serializableClass cannot be null");
            }
            this.serializableClass = serializableClass;
        }

        @Override
        public byte[] serialize(T object) {
            if (!object.getClass().equals(serializableClass)) {
                throw new IllegalArgumentException(object.getClass() + " doesnot matches " + serializableClass);
            }
            try {
                return objectMapperThreadLocal.get().writeValueAsBytes(object);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public T deserialize(byte[] channel) {
            try {
                return (T) objectMapperThreadLocal.get().readValue(channel, serializableClass);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
