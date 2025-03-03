package com.minda.iconnect.pubsub.activemq.consumer;

import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.*;
import com.minda.iconnect.platform.pubsub.SubscriberRoute.ConsumerGroup;
import com.minda.iconnect.pubsub.activemq.ActiveMQComponentFactory;
import com.minda.iconnect.pubsub.activemq.ActiveMQConf;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.DefaultErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public class ActiveMQSubscriberService extends AbstractService implements SubscriberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQSubscriberService.class);

    protected CamelContext camelContext;
    protected ActiveMQConf activeMQConf;

    protected ActiveMQComponent activeMQComponent;

    private Set<String> queueGroupIds = new HashSet<>();

    public ActiveMQSubscriberService(CamelContext camelContext, SubscriberEndpoint endpoint) {
        if (endpoint.getConfiguration() == null || !(endpoint.getConfiguration() instanceof ActiveMQConf)) {
            throw new IllegalArgumentException("ActiveMQ configuration not found in pipeline");
        }
        this.activeMQConf = (ActiveMQConf) endpoint.getConfiguration();
        this.camelContext = camelContext;
    }

    @Override
    protected void doStart() throws IOException {
        activeMQComponent = ActiveMQComponentFactory.get(activeMQConf, camelContext);
    }

    @Override
    protected void doShutdown() {
        try {
            activeMQComponent.shutdown();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void addRoute(SubscriberRoute route) {
        try {
            for (String queue : route.topics()) {
                for (ConsumerGroup consumerGroup : route.consumers()) {
                    addRoute(queue, route, consumerGroup);
                }
            }

            for (String queue : route.queues()) {
                for (ConsumerGroup consumerGroup : route.consumers()) {
                    addRoute(queue, route, consumerGroup);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private synchronized void addRoute(String queue, SubscriberRoute route, ConsumerGroup consumerGroup) throws Exception {
        if (queueGroupIds.contains(queue + "-" + consumerGroup.getGroupId())) {
            String message = String.format("queue {0} with group ID {1} already added..", queue, consumerGroup.getGroupId());
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
        queueGroupIds.add(queue + "-" + consumerGroup.getGroupId());
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                int concurrentConsumers = consumerGroup.getConcurrentConsumers();
                String acknowledgementModeName = "AUTO_ACKNOWLEDGE";
                if (route.transacted()) {
                    acknowledgementModeName = "SESSION_TRANSACTED";
                }
                activeMQConf.getCamel().getProperties().put("acknowledgementModeName", acknowledgementModeName);

                errorHandler(new DefaultErrorHandlerBuilder()
                    .maximumRedeliveries(route.maximumRedeliveries())
                    .onExceptionOccurred(exchange -> LOGGER.error(exchange.getException().getMessage(), exchange.getException())));

                RouteDefinition definition = from(activeMQConf.getID() + ":" + route.destinationType(queue).getName() + ":" + queue +
                        "?concurrentConsumers=" + concurrentConsumers + toCamelString(activeMQConf.getCamel().getProperties()))
                        .bean(route.serializer(), "deserialize");
                if (consumerGroup.getConsumerRef() != null) {
                    definition.bean(consumerGroup.getConsumerRef(), "consume");
                } else {
                    definition.bean(new ProxyConsumer<>(consumerGroup.getConsumer()));
                }
            }
        });
    }

    private String toCamelString (Map<String, Object> properties) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() != null)
                builder.append("&" + entry.getKey() + "=" + entry.getValue());
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private static class ProxyConsumer<T> implements SimpleConsumer<T> {

        private Consumer<T> target;

        public ProxyConsumer(Consumer<T> target) {
            this.target = target;
        }

        @Override
        public void consume(T message) {
            if (target instanceof SimpleConsumer) {
                ((SimpleConsumer) target).consume(message);
            } else if (target instanceof StreamingConsumer) {
                ((StreamingConsumer) target).consume(Arrays.asList(message).stream());
            } else if (target instanceof BatchConsumer) {
                ((BatchConsumer) target).consume(new Batch(Arrays.asList(message)));
            }
        }
    }
}
