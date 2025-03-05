package com.whilter.producer;

//import com.minda.iconnect.kafka.KafkaConf;
//import com.minda.iconnect.platform.core.internal.AbstractService;
//import com.minda.iconnect.platform.pubsub.*;
//import com.minda.iconnect.platform.pubsub.Producer;
//import com.minda.iconnect.platform.util.NetworkUtil;
import com.whilter.KafkaConf;
import com.whilter.core.internal.AbstractService;
import com.whilter.pubsub.*;
import com.whilter.pubsub.ProducerRoute;
import com.whilter.pubsub.Producer;
import com.whilter.util.NetworkUtil;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class KafkaProducerService extends AbstractService implements Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

    private ProducerEndpoint producerEndpoint;
    private KafkaConf kafkaConf;

    private KafkaProducer kafkaProducer;
    private ProducerRoute producerRoute;

    public KafkaProducerService(ProducerEndpoint endpoint) {
        this.producerEndpoint = endpoint;
        if (endpoint.getConfiguration() == null || !(endpoint.getConfiguration() instanceof KafkaConf)) {
            throw new IllegalArgumentException("Kafka configuration not found in pipeline");
        }
        this.kafkaConf = (KafkaConf) producerEndpoint.getConfiguration();
        this.producerRoute = producerEndpoint.getProducerRoute();
    }

    @Override
    protected void doStart() throws IOException {
        try {
            kafkaProducer = new KafkaProducer(create(producerEndpoint.getProducerRoute()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }


    @Override
    protected void doShutdown() {
        kafkaProducer.close();
    }

    private Properties create(ProducerRoute producerRoute) {
        Properties props = new Properties();
        String clientId = producerRoute.clientId() != null ? producerRoute.clientId() : kafkaConf.getClientId();
        if (clientId == null) {
            try {
                clientId = NetworkUtil.getHostName() + '-' + Thread.currentThread().getId() + '-' + Math.random();
            } catch (UnknownHostException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
        if (clientId != null) {
            props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        }
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, StringUtils.arrayToCommaDelimitedString(kafkaConf.getBrokers()));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        return props;
    }


    @Override
    public void produce(Message message) {
        produce(message, null);
    }

    @Override
    public void produce(Collection<Message> messages) {
        produce(messages, null);
    }

    @Override
    public void produce(Collection<Message> messages, AsyncCallback callback) {
        for (Message message : messages) {
            produce(message, callback);
        }
    }

    @Override
    public void produce(Stream<Message> messages) {
        messages.forEach(message -> produce(message));
    }

    @Override
    public void produce(Stream<Message> messages, AsyncCallback callback) {
        messages.forEach(message -> produce(message, callback));
    }

    @Override
    public void stop() {
        //TODO: Nothing
    }

    @Override
    public void produce(Message message, AsyncCallback callback) {
        Serializer serializer = producerEndpoint.getProducerRoute().serializer();
        byte[] data = serializer.serialize(message.getData());
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(producerRoute.producers().iterator().next().getDestination(),message.getKey(), data);
        if (callback == null) {
            kafkaProducer.send(record);
        } else {
            kafkaProducer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    LOGGER.error(exception.getMessage(), exception);
                }
                Metadata md = new Metadata();
                md.setOffset(metadata.offset());
                md.setPartition(metadata.partition());
                callback.onCompletion(md, exception);
            });
        }
    }
}
