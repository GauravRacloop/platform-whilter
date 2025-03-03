package com.minda.iconnect.pubsub.activemq.consumer;

import com.minda.iconnect.platform.core.internal.AbstractCamelComponent;
import com.minda.iconnect.platform.pubsub.SubscriberComponent;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberService;


/**
 * Created by deepakchauhan on 16/07/17.
 */
public class ActiveMQSubscriber extends AbstractCamelComponent<SubscriberEndpoint, SubscriberService> implements SubscriberComponent {

    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new ActiveMQSubscriberService(camelContext, endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }
}
