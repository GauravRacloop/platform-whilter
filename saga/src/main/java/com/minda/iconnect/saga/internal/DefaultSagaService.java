package com.minda.iconnect.saga.internal;

import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.*;
import com.minda.iconnect.platform.pubsub.serializers.JavaSerializer;
import com.minda.iconnect.saga.Change;
import com.minda.iconnect.saga.SagaEndpoint;
import com.minda.iconnect.saga.SagaService;


public class DefaultSagaService extends AbstractService implements SagaService {

    private ComponentResolver resolver;
    private SagaEndpoint endpoint;

    private Producer producer;
    private SagaConsumer sagaConsumer;
    private SubscriberService subscriberService;

    public DefaultSagaService(ComponentResolver resolver, SagaEndpoint sagaEndpoint) {
        this.resolver = resolver;
        this.endpoint = sagaEndpoint;
    }

    @Override
    protected void doStart() {
        endpoint.getModes().stream().distinct().forEach(mode -> {
            try {
                switch (mode) {
                    case PUBLISH:
                        ProducerComponent producerComponent = resolver.resolve(endpoint.getPubsubEndpoint().getProducerType(), ProducerComponent.class);
                        ProducerFactoryBean producerFactoryBean = new ProducerFactoryBean(endpoint.getPubsubEndpoint(), producerComponent, new JavaSerializer());
                        producer = producerFactoryBean.getObject();
                        break;
                    case SUBSCRIBE:
                        SubscriberComponent subscriberComponent = resolver.resolve(endpoint.getPubsubEndpoint().getConsumerType(), SubscriberComponent.class);
                        sagaConsumer = new SagaConsumer();
                        ConsumerFactoryBean consumerFactoryBean = new ConsumerFactoryBean(endpoint.getPubsubEndpoint(), subscriberComponent, new JavaSerializer(), sagaConsumer);
                        consumerFactoryBean.getObject();
                        subscriberService = consumerFactoryBean.getSubscriberService();
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doShutdown() {
        if (endpoint.getModes().contains(SagaEndpoint.Mode.PUBLISH)) {
            producer.shutdown();
        }

        if (endpoint.getModes().contains(SagaEndpoint.Mode.SUBSCRIBE)) {
            subscriberService.shutdown();
        }
    }

    @Override
    public void publish(Change change) {
        producer.produce(new Producer.Message(change.getResourceType(), change));
    }

    @Override
    public void register(String resourceType, Acceptor acceptor) {
        sagaConsumer.addAcceptor(resourceType, acceptor);
    }

    @Override
    public boolean autoStart() {
        return true;
    }
}
