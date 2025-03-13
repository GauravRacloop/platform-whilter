package com.whilter.pubsub.quartz.subscriber;


import com.whilter.pubsub.SubscriberComponent;
import com.whilter.pubsub.SubscriberEndpoint;
import com.whilter.pubsub.SubscriberService;
import com.whilter.core.internal.AbstractComponent;
/**
 * Created by mayank on 13/08/18 12:17 PM.
 */
public class QuartzSubscriber extends AbstractComponent<SubscriberEndpoint, SubscriberService>
        implements SubscriberComponent {
    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new QuartzSubscriberService(endpoint, getContext());
    }
}
