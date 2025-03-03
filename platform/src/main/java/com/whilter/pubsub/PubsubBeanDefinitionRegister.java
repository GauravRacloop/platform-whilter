package com.whilter.pubsub;

import com.whilter.conf.ConfigReader;
import com.whilter.core.ComponentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.io.Serializable;


public class PubsubBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubsubBeanDefinitionRegister.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ConfigReader configReader = ((DefaultListableBeanFactory) registry).getBean(ConfigReader.class);
        ComponentResolver resolver = ((DefaultListableBeanFactory) registry).getBean(ComponentResolver.class);
        AnnotationPubsubConfigurationSource source = new AnnotationPubsubConfigurationSource(EnablePubSub.class, metadata);

        for (Pubsub pubsub : source.values()) {
            String confId = pubsub.confId();
            String producerBean = pubsub.producerBean();
            String c2dProducerBean = pubsub.c2dProducerBean();
            String consumerBean = pubsub.consumerBean();

            Class serializerFactoryCls = pubsub.serializerFactoryCls();
            Serializer serializer;
            try {
                Class<? extends Serializable> serializableClass = pubsub.serializableClass();
                serializer = ((SerializerFactory) serializerFactoryCls.newInstance()).create(serializableClass);
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Serialization class instantiation exception: " + serializerFactoryCls + " for conf: " + confId);
                throw new RuntimeException(e);
            }

            PubsubEndpoint pubsubEndpoint = configReader.read(confId, PubsubEndpoint.class);

            if (pubsubEndpoint == null) {
                LOGGER.error("pubsubEndpoint is not available for conf: " + confId);
                throw new RuntimeException("pubsubEndpoint is not available for conf: " + confId);
            }

            PubsubConfiguration pubsubConfiguration = configReader.read(pubsubEndpoint.getMqId(), PubsubConfiguration.class);
            if (pubsubConfiguration == null) {
                LOGGER.error("pubsubConfiguration is not available for MQ ID: " + pubsubEndpoint.getMqId() + " for conf: " + confId);
                throw new RuntimeException("pubsubConfiguration is not available for MQ ID: " + pubsubEndpoint.getMqId() + " for conf: " + confId);
            }

            pubsubEndpoint.setRef(pubsubConfiguration);

            if (producerBean.trim().length() != 0) {
                ProducerComponent producerComponent = resolver.resolve(pubsubEndpoint.getProducerType(), ProducerComponent.class);
                BeanDefinitionBuilder producerBuilder = BeanDefinitionBuilder
                        .rootBeanDefinition(ProducerFactoryBean.class)
                        .addConstructorArgValue(pubsubEndpoint)
                        .addConstructorArgValue(producerComponent)
                        .addConstructorArgValue(serializer);
                registry.registerBeanDefinition(producerBean, producerBuilder.getBeanDefinition());
            }

            if (c2dProducerBean.trim().length() != 0) {
                C2DProducerComponent c2dProducerComponent = resolver.resolve(pubsubEndpoint.getC2dProducerType(), C2DProducerComponent.class);
                BeanDefinitionBuilder c2dProducerBuilder = BeanDefinitionBuilder
                        .rootBeanDefinition(C2DProducerFactoryBean.class)
                        .addConstructorArgValue(pubsubEndpoint)
                        .addConstructorArgValue(c2dProducerComponent)
                        .addConstructorArgValue(serializer);
                registry.registerBeanDefinition(c2dProducerBean, c2dProducerBuilder.getBeanDefinition());
            }

            if (consumerBean.trim().length() != 0) {
                SubscriberComponent subscriberComponent = resolver.resolve(pubsubEndpoint.getConsumerType(), SubscriberComponent.class);
                BeanDefinitionBuilder consumerBuilder = BeanDefinitionBuilder
                        .rootBeanDefinition(ConsumerFactoryBean.class)
                        .addConstructorArgValue(pubsubEndpoint)
                        .addConstructorArgValue(subscriberComponent)
                        .addConstructorArgValue(serializer)
                        .addConstructorArgValue(consumerBean);
                registry.registerBeanDefinition(consumerBean + "_proxy", consumerBuilder.getBeanDefinition());
            }
        }
    }
}
