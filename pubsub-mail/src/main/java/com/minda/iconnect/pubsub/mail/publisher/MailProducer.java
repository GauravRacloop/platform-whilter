package com.minda.iconnect.pubsub.mail.publisher;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerComponent;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;

/**
 * Created by mayank on 09/01/19 2:27 PM.
 */
public class MailProducer extends AbstractComponent<ProducerEndpoint, Producer> implements ProducerComponent {

    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new MailProducerService();
    }
}