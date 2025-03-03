package com.minda.iconnect.pubsub.eventhubs;

import com.minda.iconnect.platform.core.internal.AbstractCamelComponent;
import com.minda.iconnect.platform.pubsub.SubscriberComponent;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberService;

public class EventHubsSubscriber extends AbstractCamelComponent<SubscriberEndpoint, SubscriberService> implements SubscriberComponent {

    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new EventHubsSubscriberService(getContext(),endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

}
