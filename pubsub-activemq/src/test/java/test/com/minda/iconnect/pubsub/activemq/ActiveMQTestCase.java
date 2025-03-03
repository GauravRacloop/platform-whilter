package test.com.minda.iconnect.pubsub.activemq;

import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.platform.core.internal.DefaultComponentResolver;
import com.minda.iconnect.platform.core.internal.DefaultPlatformContext;
import com.minda.iconnect.platform.pubsub.*;
import com.minda.iconnect.pubsub.activemq.ActiveMQConf;

import java.util.HashMap;

/**
 * Created by deepakchauhan on 08/11/17.
 */
public class ActiveMQTestCase {

    public void startConsumer(SubscriberComponent component, ActiveMQConf conf) throws Exception {
        SubscriberEndpoint endpoint = new SubscriberEndpoint(conf);
        SubscriberService subscriberService = component.get(endpoint);
        subscriberService.start();

        SubscriberRoute.Builder builder = SubscriberRoute.builder();
        SubscriberRoute.ConsumerGroup consumerGroup = new SubscriberRoute.ConsumerGroup(new TestConsumer(), "deepak", 2);
        SubscriberRoute route = builder
                .topic("test")
                .serializer(new AsItIsSerializer())
                .transacted(true)
                .consumer(consumerGroup)
                .maximumRedeliveries(0)
                .build();
        subscriberService.addRoute(route);
    }

    public Producer startProducer(ProducerComponent component, ActiveMQConf conf) throws Exception {
        ProducerRoute.Builder builder = ProducerRoute.builder();
        ProducerRoute.ProducerGroup producerGroup = new ProducerRoute.ProducerGroup("test", DestinationType.QUEUE);
        producerGroup.setConcurrentProducers(2);
        builder.producers(producerGroup);
        builder.serializer(new AsItIsSerializer());
        builder.maximumRedeliveries(0);
        builder.transacted(true);
        Producer producer = component.get(new ProducerEndpoint(conf, builder.build()));
        producer.start();
        return producer;
    }

    public static void main(String[] args) throws Exception {
        PlatformContext platformContext = new DefaultPlatformContext();
        ComponentResolver resolver = new DefaultComponentResolver(platformContext);
        resolver.start();
        SubscriberComponent subscriberComponent = resolver.resolve("activemq-subscriber", SubscriberComponent.class);
        ProducerComponent producerComponent = resolver.resolve("activemq-publisher", ProducerComponent.class);

        ActiveMQConf conf = new ActiveMQConf();
        conf.setID("CCC");
        conf.setMaxConnections(2);
        conf.setMaxSessionPerConnection(10);
        conf.setMaxPollRecords(500);
        conf.setBrokerURL("failover://(tcp://localhost:61616?useInactivityMonitor=true&keepAlive=true)?initialReconnectDelay=2000&maxReconnectAttempts=3");
        conf.getAmqPoolSettings().setProperties(new HashMap<>());
        conf.getAmqPoolSettings().getProperties().put("idleTimeout", 86400000);

        ActiveMQTestCase testCase = new ActiveMQTestCase();
        testCase.startConsumer(subscriberComponent, conf);

        ActiveMQConf confp = new ActiveMQConf();
        confp.setID("PPP");
        confp.setMaxConnections(2);
        confp.setMaxSessionPerConnection(10);
        confp.setMaxPollRecords(500);
        confp.getAmqPoolSettings().setProperties(new HashMap<>());
        confp.getAmqPoolSettings().getProperties().put("idleTimeout", 86400000);
        confp.setBrokerURL("tcp://localhost:61616");
        Producer producer = testCase.startProducer(producerComponent, confp);

        for (int i = 0; i < 10; i++) {
            Producer.Message message = new Producer.Message("a" + i, "deepak" +i);
            producer.produce(message);
            Thread.sleep(100);
        }
    }

    static class TestConsumer implements SimpleConsumer<String> {

        @Override
        public void consume(String message) {
            System.out.println("message:: " + message);
        }
    }

    static class AsItIsSerializer implements Serializer<String> {

        @Override
        public byte[] serialize(String object) {
            return object.getBytes();
        }

        @Override
        public String deserialize(byte[] channel) {
            return new String(channel);
        }
    }
}
