package com.minda.iconnect.pubsub.mail.subscriber;

import com.minda.iconnect.platform.core.internal.AbstractCamelComponent;
import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.SubscriberComponent;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberService;

/**
 * Created by mayank on 09/01/19 2:32 PM.
 */
public class MailSubscriber extends AbstractCamelComponent<SubscriberEndpoint, SubscriberService> implements SubscriberComponent {

    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new MailSubscriberService(endpoint, getContext(), getCamelContext());
    }

}
