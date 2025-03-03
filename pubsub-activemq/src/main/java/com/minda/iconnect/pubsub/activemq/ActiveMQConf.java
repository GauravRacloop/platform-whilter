package com.minda.iconnect.pubsub.activemq;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.conf.SimpleConf;
import com.minda.iconnect.platform.pubsub.PubsubConfiguration;


/**
 * Created by deepakchauhan on 08/07/17.
 */
@ConfArray(ActiveMQConf[].class)
public class ActiveMQConf extends PubsubConfiguration {

    private String brokerURL;
    private int maxConnections;
    private int maxSessionPerConnection;

    private SimpleConf amqPoolSettings = new SimpleConf();
    private SimpleConf camel = new SimpleConf();

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxSessionPerConnection() {
        return maxSessionPerConnection;
    }

    public void setMaxSessionPerConnection(int maxSessionPerConnection) {
        this.maxSessionPerConnection = maxSessionPerConnection;
    }

    public SimpleConf getAmqPoolSettings() {
        return amqPoolSettings;
    }

    public void setAmqPoolSettings(SimpleConf amqPoolSettings) {
        this.amqPoolSettings = amqPoolSettings;
    }

    public SimpleConf getCamel() {
        return camel;
    }

    public void setCamel(SimpleConf camel) {
        this.camel = camel;
    }
}
