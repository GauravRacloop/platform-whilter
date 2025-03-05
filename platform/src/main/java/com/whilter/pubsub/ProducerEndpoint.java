package com.whilter.pubsub;


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
