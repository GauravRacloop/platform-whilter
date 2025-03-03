package com.minda.iconnect.pubsub.activemq.producer;

import com.minda.iconnect.platform.pubsub.Metadata;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;
import com.minda.iconnect.platform.pubsub.ProducerRoute.ProducerGroup;
import com.minda.iconnect.pubsub.activemq.consumer.ActiveMQSubscriberService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.DefaultErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.Synchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class ActiveMQProducer extends ActiveMQSubscriberService implements Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQProducer.class);

    private ProducerEndpoint producerEndpoint;
    private ProducerTemplate producerTemplate;

    public ActiveMQProducer(CamelContext camelContext, ProducerEndpoint endpoint) {
        super(camelContext, endpoint);
        this.producerEndpoint = endpoint;
    }

    @Override
    protected void doStart() throws IOException {
        super.doStart();
        try {
            initRoute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    @Override
    public void produce(Message message) {
        produce(message, null);
    }

    @Override
    public void produce(Message message, AsyncCallback callback) {
        checkAndRestart();
        for (ProducerGroup producerGroup : producerEndpoint.getProducerRoute().producers()) {
            String endpoint = "direct:" + activeMQConf.getID() + "-" + producerGroup.getDestination();

            Map<String, Object> headers = new HashMap<>();
            if (message.getKey() != null) {
                headers.put("JMSXGroupID", message.getKey());
            }
            if (message.getDelay() != 0L) {
                headers.put("AMQ_SCHEDULED_DELAY", message.getDelay());
            }
            if (callback == null) {
                producerTemplate.sendBodyAndHeaders(endpoint, message.getData(), headers);
            } else {
                producerTemplate.asyncCallback(endpoint, exchange -> {
                    exchange.getIn().setBody(message.getData());
                    exchange.getIn().getHeaders().putAll(headers);
                }, new Synchronization() {
                    @Override
                    public void onComplete(Exchange exchange) {
                        Metadata metadata = new Metadata();
                        metadata.setMessage(message.getData());
                        metadata.setKey(message.getKey());
                        callback.onCompletion(metadata, null);
                    }

                    @Override
                    public void onFailure(Exchange exchange) {
                        Metadata metadata = new Metadata();
                        metadata.setMessage(message.getData());
                        metadata.setKey(message.getKey());
                        callback.onCompletion(metadata, exchange.getException());
                    }
                });
            }
        }
    }

    private void checkAndRestart() {
        if (producerTemplate == null) {
            synchronized (this) {
                if (producerTemplate == null) {
                    try {
                        initRoute();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
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
        try {
            for (ProducerGroup producerGroup : producerEndpoint.getProducerRoute().producers()) {
                camelContext.stopRoute(producerGroup.getDestination());
            }
            producerTemplate.stop();
            producerTemplate = null;
        } catch (Exception e) {
            //TODO: EatME
        }
    }


    private void initRoute() throws Exception {
        producerTemplate = camelContext.createProducerTemplate();
        for (ProducerGroup producerGroup : producerEndpoint.getProducerRoute().producers()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    String acknowledgementModeName = "AUTO_ACKNOWLEDGE";
                    if (producerEndpoint.getProducerRoute().transacted()) {
                        acknowledgementModeName = "SESSION_TRANSACTED";
                    }
                    activeMQConf.getCamel().getProperties().put("acknowledgementModeName", acknowledgementModeName);

                    errorHandler(new DefaultErrorHandlerBuilder()
                            .maximumRedeliveries(producerEndpoint.getProducerRoute().maximumRedeliveries())
                            .onExceptionOccurred(exchange -> {
                                LOGGER.error(exchange.getException().getMessage(), exchange.getException());
                            }));

                    from("direct:" + activeMQConf.getID() + "-" + producerGroup.getDestination())
                            .bean(producerEndpoint.getProducerRoute()
                                    .serializer(), "serialize")
                            .to(activeMQConf.getID() + ":" + producerEndpoint.getProducerRoute().destinationType(producerGroup.getDestination()).getName()
                                    + ":" + producerGroup.getDestination()
                                    + "?testConnectionOnStartup=true"
                                    + toCamelString(activeMQConf.getCamel().getProperties()))
                    .routeId(producerGroup.getDestination());
                }
            });
        }
    }

    private String toCamelString(Map<String, Object> properties) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() != null)
                builder.append("&" + entry.getKey() + "=" + entry.getValue());
        }
        return builder.toString();
    }
}
