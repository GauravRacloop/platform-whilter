package com.whilter.pubsub;

import java.util.Collection;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public interface SubscriberRoute {

    static Builder builder() {
        return new SubscriberRouteImpl.BuilderImpl();
    }

    Collection<String> topics();

    Collection<String> queues();

    DestinationType destinationType(String name);

    Serializer<?> serializer();

    Collection<ConsumerGroup> consumers();

    int maximumRedeliveries();

    boolean transacted();

    CommitStrategy commitStrategy();

    String clientId();

    interface Builder {

        Builder topic(String topic, String... more);

        Builder queue(String queue, String... more);

        Builder serializer(Serializer<?> serializer);

        Builder consumer(ConsumerGroup consumerGroup, ConsumerGroup... more);

        Builder maximumRedeliveries(int maximumRedeliveries);

        Builder transacted(boolean transacted);

        Builder commitStrategy(CommitStrategy commitStrategy);

        Builder clientId(String clientId);

        SubscriberRoute build();
    }

    class CommitStrategy {
        CommitType commitType;
        BlockingPolicy blockingPolicy;
        int specifiedCommitGap;
        boolean rollbackOnException = true;

        public CommitStrategy(CommitType commitType, BlockingPolicy blockingPolicy) {
            this.commitType = commitType;
            this.blockingPolicy = blockingPolicy;
        }

        public CommitType getCommitType() {
            return commitType;
        }

        public void setCommitType(CommitType commitType) {
            this.commitType = commitType;
        }

        public BlockingPolicy getBlockingPolicy() {
            return blockingPolicy;
        }

        public void setBlockingPolicy(BlockingPolicy blockingPolicy) {
            this.blockingPolicy = blockingPolicy;
        }

        public int getSpecifiedCommitGap() {
            return specifiedCommitGap;
        }

        public void setSpecifiedCommitGap(int specifiedCommitGap) {
            this.specifiedCommitGap = specifiedCommitGap;
        }

        public boolean isRollbackOnException() {
            return rollbackOnException;
        }

        public void setRollbackOnException(boolean rollbackOnException) {
            this.rollbackOnException = rollbackOnException;
        }
    }

    enum CommitType {
        AUTO_COMMIT,
        COMMIT_POLLED,
        COMMIT_SPECIFIED_OFFSET
    }

    enum BlockingPolicy {
        SYNCH,
        ASYNCH,
        COMBINED
    }

    class ConsumerGroup {
        Consumer<?> consumer;
        String consumerRef;
        String groupId;
        int concurrentConsumers;

        public ConsumerGroup(Consumer<?> consumer, String groupId, int concurrentConsumers) {
            this.consumer = consumer;
            this.groupId = groupId;
            this.concurrentConsumers = concurrentConsumers;
        }

        public ConsumerGroup(String ref, String groupId, int concurrentConsumers) {
            this.consumerRef = ref;
            this.groupId = groupId;
            this.concurrentConsumers = concurrentConsumers;
        }

        public ConsumerGroup(Consumer<?> consumer, String groupId) {
            this.consumer = consumer;
            this.groupId = groupId;
        }

        public ConsumerGroup(String ref, String groupId) {
            this.consumerRef = ref;
            this.groupId = groupId;
        }

        public ConsumerGroup(Consumer<?> consumer, int concurrentConsumers) {
            this.consumer = consumer;
            this.concurrentConsumers = concurrentConsumers;
        }

        public ConsumerGroup(String ref, int concurrentConsumers) {
            this.consumerRef = ref;
            this.concurrentConsumers = concurrentConsumers;
        }

        public ConsumerGroup(Consumer<?> consumer) {
            this.consumer = consumer;
        }

        public ConsumerGroup(String ref) {
            this.consumerRef = ref;
        }


        public Consumer<?> getConsumer() {
            return consumer;
        }

        public String getGroupId() {
            return groupId;
        }

        public int getConcurrentConsumers() {
            return concurrentConsumers;
        }

        public String getConsumerRef() {
            return consumerRef;
        }
    }
}
