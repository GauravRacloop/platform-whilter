package com.minda.iconnect.kafka.subscriber;

import com.minda.iconnect.kafka.KafkaConf;
import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.*;
import com.minda.iconnect.platform.pubsub.Consumer;
import com.minda.iconnect.platform.util.NetworkUtil;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public class KafkaSubscriberService extends AbstractService implements SubscriberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSubscriberService.class);

    private KafkaConf kafkaConf;
    private Map<ExecutorService, Collection<ConsumerLoop>> executorServiceMap = new HashMap<>();
    private PlatformContext context;

    public KafkaSubscriberService(PlatformContext context, SubscriberEndpoint endpoint) {
        if (endpoint.getConfiguration() == null || !(endpoint.getConfiguration() instanceof KafkaConf)) {
            throw new IllegalArgumentException("Kafka configuration not found in pipeline");
        }
        this.kafkaConf = (KafkaConf) endpoint.getConfiguration();
        this.context = context;
    }

    @Override
    protected void doStart() {

    }

    @Override
    public void addRoute(SubscriberRoute route) {
        try {
            for (SubscriberRoute.ConsumerGroup consumerGroup : route.consumers()) {
                addRoute(route, consumerGroup);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void addRoute(SubscriberRoute route, SubscriberRoute.ConsumerGroup group) {
        Collection<String> topics = new ArrayList<>();
        topics.addAll(route.topics());
        topics.addAll(route.queues());

        LOGGER.info("Starting Kafka consumer on topic: {} ", route.topics());
        ExecutorService executor = Executors.newFixedThreadPool(group.getConcurrentConsumers());

        for (int j = 0; j < group.getConcurrentConsumers(); j++) {
            Consumer consumer = group.getConsumer();
            if (consumer == null) {
                consumer = context.get(group.getConsumerRef(), Consumer.class);
            }
            ConsumerLoop consumerLoop = new ConsumerLoop(create(route, group),
                   consumer,
                    route.serializer(),
                    topics, route,
                    group.getGroupId() + "-" + j);
            executor.submit(consumerLoop);
            executorServiceMap.computeIfAbsent(executor, val -> new ArrayList<>()).add(consumerLoop);
        }
    }

    @Override
    protected void doShutdown() {
        LOGGER.info("Shutting down Kafka consumers");
        for (Map.Entry<ExecutorService, Collection<ConsumerLoop>> entry : executorServiceMap.entrySet()) {
            ExecutorService executor = entry.getKey();
            executor.shutdownNow();
            if (!executor.isTerminated()) {
                entry.getValue().forEach(ConsumerLoop::shutdown);
                executor.shutdownNow();
            }
            entry.getValue().clear();
        }
        executorServiceMap.clear();
    }

    private Properties create(SubscriberRoute route, SubscriberRoute.ConsumerGroup consumerGroup) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, StringUtils.arrayToCommaDelimitedString(kafkaConf.getBrokers()));
        if (consumerGroup.getGroupId() != null) {
            props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup.getGroupId());
        }

        String clientId = route.clientId() != null ? route.clientId() : kafkaConf.getClientId();
        if (clientId == null) {
            try {
                clientId = NetworkUtil.getHostName() + '-' + Thread.currentThread().getId() + '-' + Math.random();
            } catch (UnknownHostException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        if (kafkaConf.getClientId() != null) {
            props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        }

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        if (kafkaConf.getSessionTimeoutMs() != 0) {
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConf.getSessionTimeoutMs());
            if (kafkaConf.getHeartbeatIntervalMs() == 0) {
                props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, (kafkaConf.getSessionTimeoutMs()/3));
            }
        }
        if (kafkaConf.getHeartbeatIntervalMs() != 0) {
            props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConf.getHeartbeatIntervalMs());

        }

        if (kafkaConf.getMaxPollRecords() != 0) {
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConf.getMaxPollRecords());
        }

        if (route.commitStrategy() != null && route.commitStrategy().getCommitType() != SubscriberRoute.CommitType.AUTO_COMMIT) {
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        }

        return props;
    }

    class ConsumerLoop implements Runnable {
        private KafkaConsumer<Bytes, Bytes> kafkaConsumer;
        private Properties kafkaProps;
        private Consumer consumer;
        private Serializer serializer;
        private Collection<String> topics;
        private SubscriberRoute route;
        private String id;

        public ConsumerLoop(Properties kafkaProps, Consumer consumer, Serializer serializer,
                            Collection<String> topics, SubscriberRoute route, String id) {
            this.kafkaProps = kafkaProps;
            this.consumer = consumer;
            this.serializer = serializer;
            this.topics = topics;
            this.route = route;
            this.id = id;
        }

        @Override
        public String toString() {
            return "topic:" + topics.iterator().next() + " id:" + id;
        }

        @Override
        public void run() {
            try {
                LOGGER.info("Subscribing {} to topic {}", id, topics);
                kafkaConsumer = new KafkaConsumer(kafkaProps);
                kafkaConsumer.subscribe(topics);

                while (true) {
                    long pollingFrequency = kafkaConf.getPollingFrequencyInMs() != -1 ? kafkaConf.getPollingFrequencyInMs() : Long.MAX_VALUE;
                    ConsumerRecords records = kafkaConsumer.poll(Duration.ofMillis(pollingFrequency));

                    Map<Integer, AtomicInteger> partitionOffsetCounter = new HashMap<>();
                    for (Object partition : records.partitions()) {
                        partitionOffsetCounter.put(((TopicPartition) partition).partition(), new AtomicInteger(0));
                    }

                    long specifiedCommit = kafkaConf.getMaxPollRecords();
                    if (route.commitStrategy().getCommitType() == SubscriberRoute.CommitType.COMMIT_SPECIFIED_OFFSET) {
                        specifiedCommit = route.commitStrategy().getSpecifiedCommitGap() < specifiedCommit ? route.commitStrategy().getSpecifiedCommitGap() : specifiedCommit;
                        specifiedCommit = records.count() < specifiedCommit ? records.count() : specifiedCommit;
                    }

                    boolean exceptionOccurred = false;
                    if (consumer instanceof SimpleConsumer) {
                        for (Object record : records) {
                            ConsumerRecord r = (ConsumerRecord) record;
                            try {
                                ((SimpleConsumer) consumer).consume(serializer.deserialize((byte[]) r.value()));
                                exceptionOccurred = false;
                            } catch (Exception e) {
                                exceptionOccurred = true;
                                LOGGER.error("error in consuming message for loop:" + ConsumerLoop.this.toString(), e);
                            } finally {
                                commit(partitionOffsetCounter, specifiedCommit, exceptionOccurred, r);
                            }
                        }
                    } else if (consumer instanceof BatchConsumer) {
                        List list = new ArrayList();
                        ConsumerRecord r = null;
                        for (Object record : records) {
                            r = (ConsumerRecord)record;
                            try {
                                list.add(serializer.deserialize((byte[])r.value()));
                            } catch (Exception e) {
                                exceptionOccurred = true;
                                LOGGER.error("error in de-serializing message for loop:" + ConsumerLoop.this.toString(), e);
                            }
                        }
                        if (!list.isEmpty()) {
                            try {
                                Batch batch = new Batch(list);
                                ((BatchConsumer)consumer).consume(batch);
                            } catch (Exception e) {
                                exceptionOccurred = true;
                                LOGGER.error("error in de-serializing message for loop:" + ConsumerLoop.this.toString(), e);
                            }
                            finally {
                                commit(partitionOffsetCounter, specifiedCommit, exceptionOccurred, r);
                            }
                        }
                    }

                    if (route.commitStrategy().getCommitType() != SubscriberRoute.CommitType.AUTO_COMMIT) {
                        if (route.commitStrategy().getBlockingPolicy() == SubscriberRoute.BlockingPolicy.SYNCH) {
                            kafkaConsumer.commitSync();
                        } else {
                            kafkaConsumer.commitAsync();
                        }
                    }
                }
            } catch (WakeupException e) {
                // ignore for shutdown
            } catch (Exception e) {
              LOGGER.error(e.getMessage(), e);
            } finally {
                try {
                    if (route.commitStrategy().getCommitType() != SubscriberRoute.CommitType.AUTO_COMMIT) {
                        kafkaConsumer.commitSync();
                    }
                } finally {
                    kafkaConsumer.close();
                }

            }
        }

        private void commit(Map<Integer, AtomicInteger> partitionOffsetCounter, long specifiedCommit, boolean exceptionOccurred, ConsumerRecord r) {
            boolean commit = true;
            if (exceptionOccurred && route.commitStrategy().isRollbackOnException()) {
                commit = false;
            }
            if (commit) {
                AtomicInteger current = partitionOffsetCounter.get(r.partition());
                if (current.incrementAndGet() >= specifiedCommit && route.commitStrategy().getCommitType() == SubscriberRoute.CommitType.COMMIT_SPECIFIED_OFFSET) {
                    current.set(0);
                    Map<TopicPartition, OffsetAndMetadata> partitionOffset = new HashMap<>();
                    partitionOffset.put(new TopicPartition(r.topic(), r.partition()), new OffsetAndMetadata(r.offset()));
                    if (route.commitStrategy().getBlockingPolicy() == SubscriberRoute.BlockingPolicy.SYNCH) {
                        kafkaConsumer.commitSync(partitionOffset);
                    } else {
                        kafkaConsumer.commitAsync(partitionOffset, (offsets, exception) -> {
                            if (exception != null) {
                                LOGGER.error("error in committing message for loop: " + ConsumerLoop.this.toString(), exception);
                            }
                        });
                    }
                }
            }
        }

        private void shutdown() {
            kafkaConsumer.wakeup();
        }
    }
}
