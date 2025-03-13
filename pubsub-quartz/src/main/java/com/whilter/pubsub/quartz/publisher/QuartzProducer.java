package com.whilter.pubsub.quartz.publisher;

import com.whilter.core.internal.AbstractComponent;
import com.whilter.pubsub.Producer;
import com.whilter.pubsub.ProducerComponent;
import com.whilter.pubsub.ProducerEndpoint;


public class QuartzProducer extends AbstractComponent<ProducerEndpoint, Producer> implements ProducerComponent {

    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new QuartzProducerService(endpoint, getContext());
    }
}
