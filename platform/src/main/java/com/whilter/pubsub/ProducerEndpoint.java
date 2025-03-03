package com.whilter.pubsub;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class ProducerEndpoint extends SubscriberEndpoint {

    private ProducerRoute producerRoute;

    public ProducerEndpoint(PubsubConfiguration configuration, ProducerRoute producerRoute) {
        super(configuration);
        this.producerRoute = producerRoute;
    }

    public ProducerRoute getProducerRoute() {
        return producerRoute;
    }
}
