package com.minda.iconnect.pubsub.mail.subscriber;

import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberRoute;
import com.minda.iconnect.platform.pubsub.SubscriberService;
import com.minda.iconnect.pubsub.mail.MailConf;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by mayank on 09/01/19 2:33 PM.
 */
public class MailSubscriberService extends AbstractService implements SubscriberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSubscriberService.class);

    private final SubscriberEndpoint endpoint;
    private final PlatformContext platformContext;
    private final CamelContext camelContext;
    private MailReader mailReader;

    public MailSubscriberService(SubscriberEndpoint endpoint, PlatformContext platformContext, CamelContext camelContext) {
        this.endpoint = endpoint;
        this.platformContext = platformContext;
        this.camelContext = camelContext;
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doShutdown() {
        try {
            if (camelContext.getRoute(mailReader.getRouteId()) != null) {
                camelContext.stopRoute(mailReader.getRouteId());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void addRoute(SubscriberRoute subscriberRoute) {
        Collection<SimpleConsumer> simpleConsumers = new ArrayList<>();
        Collection<String> folders = new HashSet<>();
        for (SubscriberRoute.ConsumerGroup consumerGroup : subscriberRoute.consumers()) {
            SimpleConsumer simpleConsumer = (SimpleConsumer) consumerGroup.getConsumer();
            if (simpleConsumer == null) {
                simpleConsumer = platformContext.get(consumerGroup.getConsumerRef(), SimpleConsumer.class);
                if (simpleConsumer != null) {
                    simpleConsumers.add(simpleConsumer);
                }
            }
        }

        folders.addAll(subscriberRoute.topics());
        folders.addAll(subscriberRoute.queues());

        if (folders.size() > 1) {
            throw new UnsupportedOperationException("Multiple Topics/Queues not allowed");
        }

        if (simpleConsumers.size() > 1) {
            throw new UnsupportedOperationException("Multiple Consumer/Groups not allowed");
        }

        if (folders.isEmpty()) {
            folders.add("INBOX");
        }
        mailReader = new MailReader((MailConf) endpoint.getConfiguration(), folders.iterator().next(), simpleConsumers.iterator().next());

        try {
            this.camelContext.addRoutes(mailReader);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException("Unable to Start Mail Reader Camel Route", ex);
        }
    }
}
