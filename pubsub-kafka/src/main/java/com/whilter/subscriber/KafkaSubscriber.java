package com.whilter.subscriber;



import com.whilter.pubsub.SubscriberComponent;
import com.whilter.pubsub.SubscriberEndpoint;
import com.whilter.pubsub.SubscriberService;
import com.whilter.core.internal.AbstractComponent;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public class KafkaSubscriber extends AbstractComponent<SubscriberEndpoint, SubscriberService>
        implements SubscriberComponent {

    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new KafkaSubscriberService(getContext(), endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }
}