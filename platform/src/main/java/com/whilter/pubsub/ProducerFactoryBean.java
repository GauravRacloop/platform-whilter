package com.whilter.pubsub;

import org.springframework.beans.factory.FactoryBean;

public class ProducerFactoryBean implements FactoryBean<Producer> {

    private PubsubEndpoint pubsubEndpoint;
    private ProducerComponent component;
    private Serializer serializer;

    public ProducerFactoryBean(PubsubEndpoint pubsubEndpoint, ProducerComponent component, Serializer serializer) {
        this.pubsubEndpoint = pubsubEndpoint;
        this.component = component;
        this.serializer = serializer;
    }

    @Override
    public Producer getObject() throws Exception {
        ProducerRoute.Builder builder = ProducerRoute.builder();

        if (pubsubEndpoint.getQueue() != null) {
            builder = builder.producers(new ProducerRoute.ProducerGroup(pubsubEndpoint.getQueue(), DestinationType.QUEUE));
        } else {
            builder = builder.producers(new ProducerRoute.ProducerGroup(pubsubEndpoint.getTopic(), DestinationType.TOPIC));
        }

        if (pubsubEndpoint.getMisfireStrategy() != null && !pubsubEndpoint.getMisfireStrategy().trim().isEmpty()) {
            builder.misfireStrategy(ProducerRoute.MisFireStrategy.valueOf(pubsubEndpoint.getMisfireStrategy()));
        }

        builder = builder.serializer(serializer).maximumRedeliveries(pubsubEndpoint.getMaxRedeliveries());
        Producer producer = component.get(new ProducerEndpoint(pubsubEndpoint.getRef(), builder.build()));
        producer.start();

        return producer;
    }

    @Override
    public Class<Producer> getObjectType() {
        return Producer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
