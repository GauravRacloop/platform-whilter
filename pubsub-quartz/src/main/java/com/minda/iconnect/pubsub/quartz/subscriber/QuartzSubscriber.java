package com.minda.iconnect.pubsub.quartz.subscriber;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.SubscriberComponent;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberService;

/**
 * Created by mayank on 13/08/18 12:17 PM.
 */
public class QuartzSubscriber extends AbstractComponent<SubscriberEndpoint, SubscriberService> implements SubscriberComponent {
    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new QuartzSubscriberService(endpoint, getContext());
    }
}
