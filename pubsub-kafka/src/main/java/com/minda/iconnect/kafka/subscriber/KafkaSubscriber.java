package com.minda.iconnect.kafka.subscriber;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.SubscriberComponent;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberService;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public class KafkaSubscriber extends AbstractComponent<SubscriberEndpoint, SubscriberService> implements SubscriberComponent {

    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new KafkaSubscriberService(getContext(), endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }
}