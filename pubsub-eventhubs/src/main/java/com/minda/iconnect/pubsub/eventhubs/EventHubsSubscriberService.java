package com.minda.iconnect.pubsub.eventhubs;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.*;
import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public class EventHubsSubscriberService extends AbstractService implements SubscriberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubsSubscriberService.class);
    public static final String HOST_PREFIX = "receiver";

    private EventHubsConf eventHubsConf;
    private PlatformContext context;

    private Collection<EventProcessorHost> hosts = new ArrayList<>();

    public EventHubsSubscriberService(PlatformContext context, SubscriberEndpoint endpoint) {
        if (endpoint.getConfiguration() == null || !(endpoint.getConfiguration() instanceof EventHubsConf)) {
            throw new IllegalArgumentException("EventHubs configuration not found in pipeline");
        }
        this.eventHubsConf = (EventHubsConf) endpoint.getConfiguration();
        this.context = context;
    }

    @Override
    protected void doStart() throws Exception {
        //DO NOTHING
    }

    @Override
    protected void doShutdown() {
        for (EventProcessorHost host : hosts) {
            try {
                host.unregisterEventProcessor();
            } catch (Exception e) {
                LOGGER.error("Error during un-registering the event processor.", e);
            }
        }
    }

    @Override
    public void addRoute(SubscriberRoute subscriberRoute) {
        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder(
                eventHubsConf.getNamespace(),
                eventHubsConf.getEntityPath(),
                eventHubsConf.getSharedAccessKeyName(),
                eventHubsConf.getSharedAccessKey()
        );
        String topic = subscriberRoute.topics().iterator().next();
        for (SubscriberRoute.ConsumerGroup consumerGroup : subscriberRoute.consumers()) {
            EventProcessorHost host = new EventProcessorHost(
                    EventProcessorHost.createHostName(HOST_PREFIX),
                    topic,
                    consumerGroup.getGroupId(),
                    eventHubConnectionString.toString(),
                    eventHubsConf.getAzureStorage().getConnectionString(),
                    eventHubsConf.getStorageContainer());

            SimpleConsumer consumer = (SimpleConsumer) consumerGroup.getConsumer();
            if (consumer == null) {
                consumer = context.get(consumerGroup.getConsumerRef(), SimpleConsumer.class);
            }

            EventProcessor eventProcessor = new EventProcessor(consumer, subscriberRoute.serializer());

            EventProcessorOptions options = new EventProcessorOptions();
            options.setMaxBatchSize(eventHubsConf.getMaxPollRecords());
            options.setExceptionNotification(exceptionReceivedEventArgs -> LOGGER.error(exceptionReceivedEventArgs.getException().getMessage(), exceptionReceivedEventArgs.getException()));
            try {
                host.registerEventProcessorFactory(context -> eventProcessor, options).get();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            hosts.add(host);
        }
    }

    private class EventProcessor implements IEventProcessor {

        private SimpleConsumer consumer;
        private Serializer serializer;

        public EventProcessor(SimpleConsumer consumer, Serializer serializer) {
            this.consumer = consumer;
            this.serializer = serializer;
        }

        @Override
        public void onOpen(PartitionContext context) throws Exception {
            LOGGER.debug("Opening Eventhub Consumer: " + context.getEventHubPath() + ", Partition:" + context.getPartitionId());
        }

        @Override
        public void onClose(PartitionContext context, CloseReason reason) throws Exception {
            LOGGER.debug("Closing Eventhub Consumer");
        }

        @Override
        public void onError(PartitionContext context, Throwable error) {
            LOGGER.error("Error while reading from EventHub: " + context.getEventHubPath(), error);
        }

        @Override
        public void onEvents(PartitionContext partitionContext, Iterable<EventData> events) throws Exception {
            EventData latestData = null;
            for (EventData data : events) {
                if (eventHubsConf.isIothub()) {
                    String deviceId = data.getSystemProperties().get("iothub-connection-device-id").toString();
                }

                consumer.consume(serializer.deserialize(data.getBytes()));
                latestData = data;
            }
            if (latestData != null) {
                partitionContext.checkpoint(latestData);
            }
        }
    }
}
