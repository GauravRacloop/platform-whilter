package com.whilter.pubsub;

import org.springframework.beans.factory.FactoryBean;

public class ConsumerFactoryBean implements FactoryBean<Consumer> {

    private PubsubEndpoint pubsubEndpoint;
    private SubscriberComponent component;
    private Serializer serializer;
    private String beanRef;
    private Consumer consumer;
    private String groupId;

    private SubscriberService subscriberService;


    public ConsumerFactoryBean(PubsubEndpoint pubsubEndpoint, SubscriberComponent component, Serializer serializer, String beanRef) {
        this.pubsubEndpoint = pubsubEndpoint;
        this.component = component;
        this.serializer = serializer;
        this.beanRef = beanRef;
        this.groupId = pubsubEndpoint.getGroupId();
    }

    public ConsumerFactoryBean(PubsubEndpoint pubsubEndpoint, SubscriberComponent component, Serializer serializer, Consumer consumer) {
        this.pubsubEndpoint = pubsubEndpoint;
        this.component = component;
        this.serializer = serializer;
        this.consumer = consumer;
        this.groupId = pubsubEndpoint.getGroupId();
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public ConsumerProxy getObject() throws Exception {
        SubscriberEndpoint endpoint = new SubscriberEndpoint(pubsubEndpoint.getRef());
        subscriberService = component.get(endpoint);
        subscriberService.start();

        SubscriberRoute.Builder builder = SubscriberRoute.builder();
        if (pubsubEndpoint.getQueue() == null) {
                builder = builder.topic(pubsubEndpoint.getTopic());
        } else {
            builder = builder.queue(pubsubEndpoint.getQueue());
        }

        SubscriberRoute.CommitStrategy commitStrategy = new SubscriberRoute.CommitStrategy(SubscriberRoute.CommitType.AUTO_COMMIT, SubscriberRoute.BlockingPolicy.ASYNCH);
        if (pubsubEndpoint.getCommitType() != null) {
            commitStrategy.setCommitType(pubsubEndpoint.getCommitType());
            builder = builder.transacted(true);
        }

        if (pubsubEndpoint.getBlockingPolicy() != null) {
            commitStrategy.setBlockingPolicy(pubsubEndpoint.getBlockingPolicy());
        }
        commitStrategy.setSpecifiedCommitGap(pubsubEndpoint.getSpecifiedCommitGap());
        builder = builder.commitStrategy(commitStrategy);


        SubscriberRoute.ConsumerGroup consumerGroup;
        int concurrentConsumers = pubsubEndpoint.getConcurrentConsumers()!= 0 ? pubsubEndpoint.getConcurrentConsumers() : 1;
        if (beanRef != null) {
            consumerGroup = new SubscriberRoute.ConsumerGroup(beanRef, groupId, concurrentConsumers);
        } else {
            consumerGroup = new SubscriberRoute.ConsumerGroup(consumer, groupId, concurrentConsumers);
        }
        SubscriberRoute route = builder
                .serializer(serializer)
                .consumer(consumerGroup)
                .maximumRedeliveries(pubsubEndpoint.getMaxRedeliveries())
                .build();

        subscriberService.addRoute(route);

        return new ConsumerProxy();
    }

    public SubscriberService getSubscriberService() {
        return subscriberService;
    }

    @Override
    public Class<?> getObjectType() {
        return ConsumerProxy.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
