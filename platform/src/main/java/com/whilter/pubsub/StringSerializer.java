package com.whilter.pubsub;

public class StringSerializer implements Serializer<String> {

    @Override
    public byte[] serialize(String object) {
        return object.getBytes();
    }

    @Override
    public String deserialize(byte[] channel) {
        return new String(channel);
    }
}
