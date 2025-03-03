package com.minda.iconnect.pubsub.eventhubs;

import com.microsoft.azure.sdk.iot.service.*;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.C2DProducer;
import com.minda.iconnect.platform.pubsub.ProducerEndpoint;
import com.minda.iconnect.platform.pubsub.Serializer;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public class IoTHubsProducerService extends AbstractService implements C2DProducer {

    private ProducerEndpoint producerEndpoint;
    private ServiceClient serviceClient;
    private EventHubsConf eventHubsConf;

    public IoTHubsProducerService(ProducerEndpoint endpoint) {
        if (endpoint.getConfiguration() == null || !(endpoint.getConfiguration() instanceof EventHubsConf)) {
            throw new IllegalArgumentException("EventHubs configuration not found in pipeline");
        }
        this.eventHubsConf = (EventHubsConf) endpoint.getConfiguration();
        this.producerEndpoint = endpoint;

    }

    @Override
    protected void doStart() throws Exception {
        serviceClient = ServiceClient.createFromConnectionString(eventHubsConf.getServiceConnectionString(), IotHubServiceClientProtocol.AMQPS);
        serviceClient.open();
    }

    @Override
    protected void doShutdown() {
        try {
            serviceClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void produce(String destination, Message message) {
        try {
            Serializer serializer = producerEndpoint.getProducerRoute().serializer();
            byte[] data = serializer.serialize(message.getData());
            com.microsoft.azure.sdk.iot.service.Message messageToSend = new com.microsoft.azure.sdk.iot.service.Message(data);
            messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);

            serviceClient.send(destination, messageToSend);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void produce(String destination, Collection<Message> messages) {
        for (Message message : messages) {
            produce(destination, message);
        }
    }

    @Override
    public void produce(String destination, Stream<Message> messages) {
        messages.forEach(message -> produce(destination, message));
    }

    @Override
    public void produce(String destination, Message message, AsyncCallback callback) {
        produce(destination, message);
    }

    @Override
    public void produce(String destination, Collection<Message> messages, AsyncCallback callback) {
        for (Message message : messages) {
            produce(destination, message);
        }
    }

    @Override
    public void produce(String destination, Stream<Message> messages, AsyncCallback callback) {
        messages.forEach(message -> produce(destination, message));
    }

    @Override
    public void stop() {
        try {
            serviceClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
