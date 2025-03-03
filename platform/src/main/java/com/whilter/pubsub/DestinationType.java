package com.whilter.pubsub;

public enum DestinationType {
    QUEUE ("queue"), TOPIC ("topic");

    private String name;

    DestinationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
