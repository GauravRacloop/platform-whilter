package com.whilter.pubsub;

import com.whilter.core.Endpoint;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public class SubscriberEndpoint implements Endpoint {

    private PubsubConfiguration configuration;

    public SubscriberEndpoint(PubsubConfiguration configuration) {
        this.configuration = configuration;
    }

    public PubsubConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return configuration.getID();
    }
}
