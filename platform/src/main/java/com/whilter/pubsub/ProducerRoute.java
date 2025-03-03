package com.whilter.pubsub;

import java.util.Collection;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface ProducerRoute {

    static Builder builder() {
        return new ProducerRouteImpl.BuilderImpl();
    }

    Serializer<?> serializer();

    Collection<ProducerGroup> producers();

    Collection<String> topics();

    Collection<String> queues();

    DestinationType destinationType(String name);

    int maximumRedeliveries();

    boolean transacted();

    MisFireStrategy misfireStrategy();

    String clientId();

    interface Builder {

        ProducerRoute.Builder topic(String topic, String... more);

        ProducerRoute.Builder queue(String queue, String... more);

        Builder serializer(Serializer<?> serializer);

        Builder producers(ProducerGroup producerGroup, ProducerGroup... more);

        Builder maximumRedeliveries(int maximumRedeliveries);

        Builder transacted(boolean transacted);

        Builder misfireStrategy(MisFireStrategy strategy);

        Builder clientId(String clientId);

        ProducerRoute build();

    }

    class ProducerGroup {
        String destination;
        DestinationType destinationType;
        int concurrentProducers;

        public ProducerGroup(String destination, DestinationType destinationType) {
            this.destination = destination;
            this.destinationType = destinationType;
        }

        public String getDestination() {
            return destination;
        }

        public int getConcurrentProducers() {
            return concurrentProducers;
        }

        public void setConcurrentProducers(int concurrentProducers) {
            this.concurrentProducers = concurrentProducers;
        }
    }

    enum MisFireStrategy {
        FIRE_NOW, IGNORE
    }

}
