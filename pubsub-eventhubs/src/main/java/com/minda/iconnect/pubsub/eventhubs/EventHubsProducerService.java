package com.minda.iconnect.pubsub.eventhubs;

import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;

import java.util.Collection;
import java.util.stream.Stream;

public class EventHubsProducerService extends AbstractService implements Producer {

    private ProducerEndpoint producerEndpoint;

    public EventHubsProducerService(ProducerEndpoint producerEndpoint) {
        this.producerEndpoint = producerEndpoint;
    }

    @Override
    protected void doStart() throws Exception {

    }

    @Override
    protected void doShutdown() {

    }

    @Override
    public void produce(Message message) {

    }

    @Override
    public void produce(Collection<Message> messages) {

    }

    @Override
    public void produce(Stream<Message> messages) {

    }

    @Override
    public void produce(Message message, AsyncCallback callback) {

    }

    @Override
    public void produce(Collection<Message> messages, AsyncCallback callback) {

    }

    @Override
    public void produce(Stream<Message> messages, AsyncCallback callback) {

    }

    @Override
    public void stop() {

    }
}
