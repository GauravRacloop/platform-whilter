package com.minda.iconnect.pubsub.sftp.subscriber;

import com.minda.iconnect.platform.core.internal.AbstractCamelComponent;
import com.minda.iconnect.platform.pubsub.SubscriberComponent;
import com.minda.iconnect.platform.pubsub.SubscriberEndpoint;
import com.minda.iconnect.platform.pubsub.SubscriberService;

/**
 * @author thanos on 21/05/19
 */
public class SftpSubscriber extends AbstractCamelComponent<SubscriberEndpoint, SubscriberService> implements SubscriberComponent {
    @Override
    protected SubscriberService doGet(SubscriberEndpoint endpoint) {
        return new SftpSubscriberService(endpoint, getContext(), getCamelContext());
    }
}
