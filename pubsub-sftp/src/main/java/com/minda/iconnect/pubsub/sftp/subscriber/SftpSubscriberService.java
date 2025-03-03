package com.minda.iconnect.pubsub.sftp.subscriber;

import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberRoute;
import com.minda.iconnect.platform.pubsub.SubscriberService;
import com.minda.iconnect.pubsub.sftp.SftpConf;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author thanos on 21/05/19
 */
public class SftpSubscriberService extends AbstractService implements SubscriberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpSubscriberService.class);

    private final SubscriberEndpoint endpoint;
    private final PlatformContext platformContext;
    private final CamelContext camelContext;

    public SftpSubscriberService(SubscriberEndpoint endpoint, PlatformContext platformContext,
                                 CamelContext camelContext) {
        this.endpoint = endpoint;
        this.platformContext = platformContext;
        this.camelContext = camelContext;
    }

    @Override
    protected void doStart()  {
    }

    @Override
    protected void doShutdown() {
    }

    @Override
    public void addRoute(SubscriberRoute subscriberRoute) {

        Collection<SimpleConsumer> simpleConsumers = new ArrayList<>();
        for (SubscriberRoute.ConsumerGroup consumerGroup : subscriberRoute.consumers()) {
            SimpleConsumer simpleConsumer = (SimpleConsumer) consumerGroup.getConsumer();
            if (simpleConsumer == null) {
                simpleConsumer = platformContext.get(consumerGroup.getConsumerRef(), SimpleConsumer.class);
                if (simpleConsumer != null) {
                    simpleConsumers.add(simpleConsumer);
                }
            }
        }

        if (simpleConsumers.size() > 1) {
            throw new UnsupportedOperationException("Multiple Consumer/Groups not allowed");
        }

        try {
            camelContext.addRoutes(new SftpReader((SftpConf) endpoint.getConfiguration(), simpleConsumers.iterator().next()));
        } catch (Exception e) {
            LOGGER.error("Unable to start Start SFTP");
        }
    }
}
