package com.minda.iconnect.pubsub.sftp.publisher;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerComponent;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;

/**
 * @author thanos on 21/05/19
 */
public class SftpProducer extends AbstractComponent<ProducerEndpoint, Producer> implements ProducerComponent {
    @Override
    protected Producer doGet(ProducerEndpoint endpoint) {
        return new SftpProducerService();
    }
}
