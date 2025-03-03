package com.minda.iconnect.pubsub.quartz.publisher;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerComponent;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;

/**
 * Created by mayank on 10/08/18 6:12 PM.
 */
public class QuartzProducer extends AbstractComponent<ProducerEndpoint, Producer> implements ProducerComponent {

    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new QuartzProducerService(endpoint, getContext());
    }
}
