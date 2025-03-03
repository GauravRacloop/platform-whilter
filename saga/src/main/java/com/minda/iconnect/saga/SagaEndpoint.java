package com.minda.iconnect.saga;

import com.minda.iconnect.platform.core.Endpoint;
import com.minda.iconnect.platform.pubsub.PubsubEndpoint;

import java.util.ArrayList;
import java.util.Collection;

public class SagaEndpoint implements Endpoint {

    private PubsubEndpoint pubsubEndpoint;

    private Collection<Mode> modes = new ArrayList<>();

    public SagaEndpoint(PubsubEndpoint pubsubEndpoint) {
        this.pubsubEndpoint = pubsubEndpoint;
        this.modes.add(Mode.PUBLISH);
    }

    public PubsubEndpoint getPubsubEndpoint() {
        return pubsubEndpoint;
    }

    public Collection<Mode> getModes() {
        return modes;
    }

    public void setModes(Collection<Mode> modes) {
        this.modes = modes;
    }

    @Override
    public String toString() {
        return pubsubEndpoint.getID();
    }

    public enum Mode {
        PUBLISH, SUBSCRIBE
    }
}
