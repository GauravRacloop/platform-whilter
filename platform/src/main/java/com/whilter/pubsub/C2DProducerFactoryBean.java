package com.whilter.pubsub;

import org.springframework.beans.factory.FactoryBean;

public class C2DProducerFactoryBean implements FactoryBean<C2DProducer> {

    private PubsubEndpoint pubsubEndpoint;
    private C2DProducerComponent component;
    private Serializer serializer;

    public C2DProducerFactoryBean(PubsubEndpoint pubsubEndpoint, C2DProducerComponent component, Serializer serializer) {
        this.pubsubEndpoint = pubsubEndpoint;
        this.component = component;
        this.serializer = serializer;
    }

    @Override
    public C2DProducer getObject() throws Exception {
        ProducerRoute.Builder builder = ProducerRoute.builder();

        String topic = pubsubEndpoint.getTopic();
        if (topic == null) {
            topic = pubsubEndpoint.getQueue();
        }
        builder = builder.producers(new ProducerRoute.ProducerGroup(topic, DestinationType.TOPIC));

        builder = builder.serializer(serializer).maximumRedeliveries(pubsubEndpoint.getMaxRedeliveries());
        C2DProducer producer = component.get(new ProducerEndpoint(pubsubEndpoint.getRef(), builder.build()));
        producer.start();

        return producer;
    }

    @Override
    public Class<C2DProducer> getObjectType() {
        return C2DProducer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
