package com.whilter.pubsub;

import java.util.*;

/**
 * Created by deepakchauhan on 16/07/17.
 */
class SubscriberRouteImpl implements SubscriberRoute {

    private Collection<String> topics = new ArrayList<>();
    private Collection<String> queues = new ArrayList<>();
    private int maximumRedeliveries;
    private Serializer<?> serializer;
    private Collection<SubscriberRoute.ConsumerGroup> consumerGroups = new ArrayList<>();
    private boolean transacted;
    private CommitStrategy commitStrategy = new CommitStrategy(CommitType.AUTO_COMMIT, BlockingPolicy.ASYNCH);
    private String clientId;

    private Map<String, DestinationType> destinationTypeMap = new HashMap<>();

    @Override
    public Collection<String> topics() {
        return topics;
    }

    @Override
    public Collection<String> queues() {
        return queues;
    }

    @Override
    public int maximumRedeliveries() {
        return maximumRedeliveries;
    }

    @Override
    public Serializer<?> serializer() {
        return serializer;
    }

    @Override
    public Collection<SubscriberRoute.ConsumerGroup> consumers() {
        return consumerGroups;
    }

    @Override
    public DestinationType destinationType(String name) {
        return destinationTypeMap.get(name);
    }

    @Override
    public CommitStrategy commitStrategy() {
        return commitStrategy;
    }

    @Override
    public boolean transacted() {
        return transacted;
    }

    @Override
    public String clientId() {
        return clientId;
    }


    static class BuilderImpl implements Builder {
        private SubscriberRouteImpl subscriberBuilder;

        public BuilderImpl() {
            this.subscriberBuilder = new SubscriberRouteImpl();
        }

        @Override
        public SubscriberRoute.Builder topic(String topic, String... more) {
            subscriberBuilder.topics.add(topic);
            subscriberBuilder.destinationTypeMap.put(topic, DestinationType.TOPIC);
            if (more != null && more.length > 0) {
                for (String tpc : more) {
                    subscriberBuilder.topics.add(tpc);
                    subscriberBuilder.destinationTypeMap.put(tpc, DestinationType.TOPIC);
                }
            }
            return this;
        }

        @Override
        public SubscriberRoute.Builder queue(String queue, String... more) {
            subscriberBuilder.queues.add(queue);
            subscriberBuilder.destinationTypeMap.put(queue, DestinationType.QUEUE);
            if (more != null && more.length > 0) {
                for (String que : more) {
                    subscriberBuilder.queues.add(que);
                    subscriberBuilder.destinationTypeMap.put(que, DestinationType.QUEUE);
                }
            }
            return this;
        }

        @Override
        public Builder maximumRedeliveries(int maximumRedeliveries) {
            subscriberBuilder.maximumRedeliveries = maximumRedeliveries;
            return this;
        }

        @Override
        public Builder serializer(Serializer<?> serializer) {
            subscriberBuilder.serializer = serializer;
            return this;
        }

        @Override
        public Builder consumer(ConsumerGroup consumerGroup, ConsumerGroup... more) {
            subscriberBuilder.consumerGroups.add(consumerGroup);
            if (more != null && more.length > 0) {
                subscriberBuilder.consumerGroups.addAll(Arrays.asList(more));
            }
            return this;
        }

        @Override
        public Builder transacted(boolean transacted) {
            subscriberBuilder.transacted = transacted;
            return this;
        }

        @Override
        public Builder commitStrategy(CommitStrategy commitStrategy) {
            subscriberBuilder.commitStrategy = commitStrategy;
            return this;
        }

        @Override
        public Builder clientId(String clientId) {
            subscriberBuilder.clientId = clientId;
            return this;
        }

        @Override
        public SubscriberRoute build() {
            return subscriberBuilder;
        }
    }

}
