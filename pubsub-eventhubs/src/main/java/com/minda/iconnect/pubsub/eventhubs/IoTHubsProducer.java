package com.minda.iconnect.pubsub.eventhubs;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.*;

public class IoTHubsProducer extends AbstractComponent<ProducerEndpoint, C2DProducer> implements C2DProducerComponent {

    @Override
    protected C2DProducer doGet(ProducerEndpoint endpoint) {
        return new IoTHubsProducerService(endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

}
