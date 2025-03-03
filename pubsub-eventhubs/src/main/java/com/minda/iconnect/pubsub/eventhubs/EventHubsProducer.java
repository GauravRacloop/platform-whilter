package com.minda.iconnect.pubsub.eventhubs;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerComponent;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;

public class EventHubsProducer extends AbstractComponent<ProducerEndpoint, Producer> implements ProducerComponent {

    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new EventHubsProducerService(endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

}
