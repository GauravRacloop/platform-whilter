package com.whilter.pubsub;


/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface Serializer<T> {

    byte[] serialize(T object);

    T deserialize(byte[] channel);
}
