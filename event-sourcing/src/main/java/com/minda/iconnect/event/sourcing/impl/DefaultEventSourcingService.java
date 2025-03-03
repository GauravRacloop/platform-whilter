package com.minda.iconnect.event.sourcing.impl;

import com.minda.iconnect.event.sourcing.EventSourcing;
import com.minda.iconnect.event.sourcing.EventSourcingService;
import com.minda.iconnect.event.sourcing.processors.C2DProcessor;
import com.minda.iconnect.event.sourcing.processors.KeyStoreDeleteProcessor;
import com.minda.iconnect.kafka.KafkaConf;
import com.whilter.core.internal.AbstractService;
import com.whilter.pubsub.*;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.*;

import java.util.Properties;

/**
 * @author jaspreet on 16/01/19
 */
public class DefaultEventSourcingService extends AbstractService implements EventSourcingService {

    public static final String SOURCE_TOPIC = "SOURCE";
    public static final String DELETE_TOPIC = "DELETE";
    public static final String C2D_PROCESSOR = "c2dProcessor";
    public static final String DELETE_PROCESSOR = "keyStoreDeleteProcessor";

    private final String id;
    private final String stateStore;
    private final ProducerComponent c2dComponent;
    private final ProducerComponent deleteComponent;
    private final KafkaConf kafkaConf;
    private Producer c2dProducer;
    private Producer deleteProducer;
    private final EventSourcing eventSourcing;

    private KafkaStreams kafkaStreams;
    ReadOnlyKeyValueStore<String, String> keyValueStore;

    public DefaultEventSourcingService(EventSourcing eventSourcing, KafkaConf kafkaConf,
                                       ProducerComponent c2dComponent, ProducerComponent deleteComponent) {
        this.id = eventSourcing.getID();
        this.stateStore = eventSourcing.getStateStore();
        this.kafkaConf = kafkaConf;
        this.eventSourcing = eventSourcing;
        this.c2dComponent = c2dComponent;
        this.deleteComponent = deleteComponent;
    }


    @Override
    public void write(String key, String value) {
        c2dProducer.produce(new Producer.Message(key + System.currentTimeMillis(), value));
    }

    @Override
    public void read(String key, Function function) {
        if (keyValueStore == null) {
            keyValueStore = kafkaStreams.store(stateStore, QueryableStoreTypes.keyValueStore());
        }
        //Need to change with config value
        if (eventSourcing.getQueryType() != null && eventSourcing.getQueryType() == EventSourcing.QueryType.KEY) {
            String value = keyValueStore.get(key);
            if (value != null) {
                function.invoke(value);
                deleteProducer.produce(new Producer.Message(key, ""));
            }
        } else {
            KeyValueIterator<String, String> valueIterator = keyValueStore.range(key + "0", key + System.currentTimeMillis());

            while (valueIterator.hasNext()) {
                KeyValue<String, String> next = valueIterator.next();
                if (next.value != null) {
                    function.invoke(next.value);
                    deleteProducer.produce(new Producer.Message(next.key, ""));
                }
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        this.c2dProducer = createProducer(eventSourcing.getC2d(), c2dComponent);
        this.deleteProducer = createProducer(eventSourcing.getDelete(), deleteComponent);


        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, id);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConf.getBrokers()[0]);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StoreBuilder<KeyValueStore<String, String>> store =
                Stores.keyValueStoreBuilder(
                        Stores.inMemoryKeyValueStore(stateStore),
                        Serdes.String(),
                        Serdes.String());

        Topology topology = new Topology();
        topology.addSource(SOURCE_TOPIC, eventSourcing.getC2d().getTopic())
                .addSource(DELETE_TOPIC, eventSourcing.getDelete().getTopic())
                .addProcessor(C2D_PROCESSOR, () -> new C2DProcessor(stateStore), SOURCE_TOPIC)
                .addProcessor(DELETE_PROCESSOR, () -> new KeyStoreDeleteProcessor(stateStore), DELETE_TOPIC)
                .addStateStore(store, C2D_PROCESSOR, DELETE_PROCESSOR);

        kafkaStreams = new KafkaStreams(topology, props);
        kafkaStreams.start();
    }

    @Override
    protected void doShutdown() {
        kafkaStreams.close();
    }

    private Producer createProducer(PubsubEndpoint pubsubEndpoint, ProducerComponent component) throws Exception {
        ProducerRoute.Builder builder = ProducerRoute.builder();
        if (pubsubEndpoint.getQueue() != null) {
            builder = builder.producers(new ProducerRoute.ProducerGroup(pubsubEndpoint.getQueue(), DestinationType.QUEUE));
        } else {
            builder = builder.producers(new ProducerRoute.ProducerGroup(pubsubEndpoint.getTopic(), DestinationType.TOPIC));
        }

        if (pubsubEndpoint.getMisfireStrategy() != null && !pubsubEndpoint.getMisfireStrategy().trim().isEmpty()) {
            builder.misfireStrategy(ProducerRoute.MisFireStrategy.valueOf(pubsubEndpoint.getMisfireStrategy()));
        }

        builder = builder.serializer(new StringSerializer()).maximumRedeliveries(pubsubEndpoint.getMaxRedeliveries());
        Producer producer = component.get(new ProducerEndpoint(kafkaConf, builder.build()));
        producer.start();
        return producer;
    }

    @Override
    public boolean autoStart() {
        return true;
    }

}
