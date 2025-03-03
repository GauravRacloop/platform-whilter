package com.whilter.pubsub;

import java.util.*;

/**
 * Created by deepakchauhan on 12/08/17.
 */
class ProducerRouteImpl implements ProducerRoute {

    private int maximumRedeliveries;
    private Serializer<?> serializer;
    private Collection<ProducerGroup> producerGroups = new ArrayList<>();
    private boolean transacted;
    private Collection<String> topics = new ArrayList<>();
    private Collection<String> queues = new ArrayList<>();
    private MisFireStrategy misfireStrategy;
    private String clientId;

    private Map<String, DestinationType> destinationTypeMap = new HashMap<>();

    @Override
    public Serializer<?> serializer() {
        return serializer;
    }

    @Override
    public int maximumRedeliveries() {
        return maximumRedeliveries;
    }

    @Override
    public Collection<ProducerGroup> producers() {
        return producerGroups;
    }

    @Override
    public boolean transacted() {
        return transacted;
    }

    @Override
    public Collection<String> topics() {
        return topics;
    }

    @Override
    public Collection<String> queues() {
        return queues;
    }

    @Override
    public MisFireStrategy misfireStrategy() {
        return misfireStrategy;
    }


   @Override
    public DestinationType destinationType(String name) {
        return destinationTypeMap.get(name);
    }

    @Override
    public String clientId() {
        return clientId;
    }

    static class BuilderImpl implements Builder {

        ProducerRouteImpl route = new ProducerRouteImpl();

        @Override
        public Builder serializer(Serializer<?> serializer) {
            route.serializer = serializer;
            return this;
        }

        @Override
        public Builder maximumRedeliveries(int maximumRedeliveries) {
            route.maximumRedeliveries = maximumRedeliveries;
            return this;
        }

        @Override
        public Builder producers(ProducerGroup producerGroup, ProducerGroup... more) {
            route.producerGroups.add(producerGroup);
            route.destinationTypeMap.put(producerGroup.getDestination(), producerGroup.destinationType);
            if (more != null && more.length > 0) {
                route.producerGroups.addAll(Arrays.asList(more));
                for (ProducerGroup group : more) {
                    route.destinationTypeMap.put(group.getDestination(), group.destinationType);
                }
            }
            return this;
        }

        @Override
        public Builder transacted(boolean transacted) {
            route.transacted = transacted;
            return this;
        }

        @Override
        public Builder misfireStrategy(MisFireStrategy strategy) {
            route.misfireStrategy = strategy;
            return this;
        }

        @Override
        public ProducerRoute.Builder topic(String topic, String... more) {
            route.topics.add(topic);
            route.destinationTypeMap.put(topic, DestinationType.TOPIC);
            if (more != null && more.length > 0) {
                for (String tpc : more) {
                    route.topics.add(tpc);
                    route.destinationTypeMap.put(tpc, DestinationType.TOPIC);
                }
            }
            return this;
        }

        @Override
        public Builder queue(String queue, String... more) {
            route.queues.add(queue);
            route.destinationTypeMap.put(queue, DestinationType.QUEUE);
            if (more != null && more.length > 0) {
                for (String que : more) {
                    route.queues.add(que);
                    route.destinationTypeMap.put(que, DestinationType.QUEUE);
                }
            }
            return this;
        }

        @Override
        public Builder clientId(String clientId) {
            route.clientId = clientId;
            return this;
        }

        @Override
        public ProducerRoute build() {
            return route;
        }
    }
}
