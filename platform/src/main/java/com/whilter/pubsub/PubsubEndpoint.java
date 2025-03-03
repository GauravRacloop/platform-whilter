package com.whilter.pubsub;

import com.whilter.conf.ConfArray;
import com.whilter.conf.internal.AbstractConfiguration;

/**
 * Created by deepakchauhan on 27/09/17.
 */
@ConfArray(PubsubEndpoint[].class)
public class PubsubEndpoint extends AbstractConfiguration {

    private String type;
    private String consumerType;
    private String producerType;
    private String c2dProducerType;
    private PubsubConfiguration ref;
    private String mqId;
    private String queue;
    private String topic;
    private String groupId;
    private int concurrentConsumers;
    private int concurrentProducers;
    private int maxRedeliveries;
    private SubscriberRoute.CommitType commitType;
    private SubscriberRoute.BlockingPolicy blockingPolicy;
    private int specifiedCommitGap;
    private String misfireStrategy;

    public String getType() {
        if (type == null) {
            type = consumerType != null ? consumerType : producerType;
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PubsubConfiguration getRef() {
        return ref;
    }

    public void setRef(PubsubConfiguration ref) {
        this.ref = ref;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public int getConcurrentProducers() {
        return concurrentProducers;
    }

    public void setConcurrentProducers(int concurrentProducers) {
        this.concurrentProducers = concurrentProducers;
    }

    public int getMaxRedeliveries() {
        return maxRedeliveries;
    }

    public void setMaxRedeliveries(int maxRedeliveries) {
        this.maxRedeliveries = maxRedeliveries;
    }

    public SubscriberRoute.CommitType getCommitType() {
        return commitType;
    }

    public void setCommitType(SubscriberRoute.CommitType commitType) {
        this.commitType = commitType;
    }

    public SubscriberRoute.BlockingPolicy getBlockingPolicy() {
        return blockingPolicy;
    }

    public void setBlockingPolicy(SubscriberRoute.BlockingPolicy blockingPolicy) {
        this.blockingPolicy = blockingPolicy;
    }

    public int getSpecifiedCommitGap() {
        return specifiedCommitGap;
    }

    public void setSpecifiedCommitGap(int specifiedCommitGap) {
        this.specifiedCommitGap = specifiedCommitGap;
    }

    public String getMqId() {
        return mqId;
    }

    public void setMqId(String mqId) {
        this.mqId = mqId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getProducerType() {
        return producerType;
    }

    public void setProducerType(String producerType) {
        this.producerType = producerType;
    }

    public String getMisfireStrategy() {
        return misfireStrategy;
    }

    public void setMisfireStrategy(String misfireStrategy) {
        this.misfireStrategy = misfireStrategy;
    }

    public String getC2dProducerType() {
        return c2dProducerType;
    }

    public void setC2dProducerType(String c2dProducerType) {
        this.c2dProducerType = c2dProducerType;
    }
}
