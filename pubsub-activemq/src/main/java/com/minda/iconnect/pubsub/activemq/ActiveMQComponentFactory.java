package com.minda.iconnect.pubsub.activemq;

import com.minda.iconnect.platform.util.BeanUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class ActiveMQComponentFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQComponentFactory.class);

    private static Map<String, ActiveMQComponent> componentMap = new HashMap<>();

    public synchronized static ActiveMQComponent get(ActiveMQConf activeMQConf, CamelContext camelContext) {
        try {
            ActiveMQComponent activeMQComponent = componentMap.get(activeMQConf.getID());
            if (activeMQComponent != null) return activeMQComponent;

            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            activeMQConnectionFactory.setBrokerURL(activeMQConf.getBrokerURL());
            if (activeMQConf.getMaxPollRecords() != 0) {
                ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
                prefetchPolicy.setAll(activeMQConf.getMaxPollRecords());
                activeMQConnectionFactory.setPrefetchPolicy(prefetchPolicy);
            }

            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            redeliveryPolicy.setMaximumRedeliveries(activeMQConf.getRetries());
            redeliveryPolicy.setQueue("*");
            activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);

            activeMQConnectionFactory.setTrustAllPackages(true);

            setAdvancedProperties(activeMQConnectionFactory, activeMQConf.getProperties());

            PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
            pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);

            if (activeMQConf.getMaxConnections() != 0) {
                pooledConnectionFactory.setMaxConnections(activeMQConf.getMaxConnections());
            }

            if (activeMQConf.getMaxSessionPerConnection() != 0) {
                pooledConnectionFactory.setMaximumActiveSessionPerConnection(activeMQConf.getMaxSessionPerConnection());
            }

            setAdvancedProperties(pooledConnectionFactory, activeMQConf.getAmqPoolSettings().getProperties());

            JmsConfiguration jmsConfiguration = new JmsConfiguration();
            jmsConfiguration.setConnectionFactory(pooledConnectionFactory);

            activeMQComponent = ActiveMQComponent.activeMQComponent();
            activeMQComponent.setConfiguration(jmsConfiguration);
            camelContext.addComponent(activeMQConf.getID(), activeMQComponent);
            componentMap.put(activeMQConf.getID(), activeMQComponent);

            return activeMQComponent;

        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static void setAdvancedProperties(Object instance, Map<String, Object> properties)
            throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            try {
                BeanUtils.setProperty(instance, entry.getKey().toString(), entry.getValue());
            } catch (BeansException | IllegalArgumentException e) {
                //Eating exception
                LOGGER.warn("Property with name {0} not found for class {1}", entry.getKey(), instance.getClass());
            }
        }
    }
}
