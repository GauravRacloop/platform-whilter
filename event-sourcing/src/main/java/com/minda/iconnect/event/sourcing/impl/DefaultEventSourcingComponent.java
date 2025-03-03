package com.minda.iconnect.event.sourcing.impl;

import com.minda.iconnect.event.sourcing.EventSourcing;
import com.minda.iconnect.event.sourcing.EventSourcingComponent;
import com.minda.iconnect.event.sourcing.EventSourcingEndPoint;
import com.minda.iconnect.event.sourcing.EventSourcingService;
import com.minda.iconnect.kafka.KafkaConf;
import com.minda.iconnect.platform.conf.ConfigReader;
import com.whilter.core.ComponentResolver;
import com.whilter.core.PlatformContext;
import com.whilter.core.internal.AbstractComponent;
import com.whilter.pubsub.*;

/**
 * @author jaspreet on 16/01/19
 */
public class DefaultEventSourcingComponent extends AbstractComponent<EventSourcingEndPoint, EventSourcingService> implements EventSourcingComponent {

    @Override
    protected EventSourcingService doGet(EventSourcingEndPoint endpoint) {
        EventSourcing conf = endpoint.getEventSourcing();
        PlatformContext context = getContext();
        ConfigReader configReader = context.get(ConfigReader.class);
        KafkaConf kafkaConf = configReader.read(conf.getC2d().getMqId(), KafkaConf.class);
        ComponentResolver componentResolver = context.get(ComponentResolver.class);
        ProducerComponent c2dComponent = componentResolver.resolve(conf.getC2d().getProducerType(), ProducerComponent.class);
        ProducerComponent deleteComponent = componentResolver.resolve(conf.getC2d().getProducerType(), ProducerComponent.class);
        return new DefaultEventSourcingService(conf, kafkaConf, c2dComponent, deleteComponent);
    }


}
