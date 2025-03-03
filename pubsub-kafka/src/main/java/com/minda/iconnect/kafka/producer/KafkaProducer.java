package com.minda.iconnect.kafka.producer;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerComponent;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;
import com.minda.iconnect.platform.pubsub.ProducerRoute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class KafkaProducer extends AbstractComponent<ProducerEndpoint, Producer>
        implements ProducerComponent {

    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new KafkaProducerService(endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(ProducerEndpoint endpoint) {
        ArrayList<ProducerRoute.ProducerGroup> producers = new ArrayList<>(endpoint.getProducerRoute().producers());
        Collections.sort(producers, Comparator.comparing(ProducerRoute.ProducerGroup::getDestination));
        StringBuilder finalID = new StringBuilder();
        for (ProducerRoute.ProducerGroup producer : producers) {
            finalID.append(producer.getDestination() + "_");
        }
        return endpoint.getConfiguration().getID() + "_" + finalID.toString();
    }
}
