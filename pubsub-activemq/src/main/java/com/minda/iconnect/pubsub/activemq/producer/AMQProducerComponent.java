package com.minda.iconnect.pubsub.activemq.producer;

import com.minda.iconnect.platform.core.internal.AbstractCamelComponent;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerComponent;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;
import com.minda.iconnect.platform.pubsub.ProducerRoute;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class AMQProducerComponent extends AbstractCamelComponent<ProducerEndpoint, Producer>
        implements ProducerComponent {

    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new ActiveMQProducer(camelContext, endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(ProducerEndpoint endpoint) {
        ArrayList<ProducerRoute.ProducerGroup> producers = new ArrayList<>(endpoint.getProducerRoute().producers());
        Collections.sort(producers, (o1, o2) -> o1.getDestination().compareTo(o2.getDestination()));
        StringBuilder finalID = new StringBuilder();
        for (ProducerRoute.ProducerGroup producer : producers) {
            finalID.append(producer.getDestination() + "_");
        }
        return endpoint.getConfiguration().getID() + "_" + finalID.toString();
    }
}
