package com.minda.iconnect.kafka;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.pubsub.PubsubConfiguration;

/**
 * Created by deepakchauhan on 09/07/17.
 */
@ConfArray(KafkaConf[].class)
public class KafkaConf extends PubsubConfiguration {

    private String[] quorum;
    private String[] brokers;

    public String[] getQuorum() {
        return quorum;
    }

    public void setQuorum(String[] quorum) {
        this.quorum = quorum;
    }

    public String[] getBrokers() {
        return brokers;
    }

    public void setBrokers(String[] brokers) {
        this.brokers = brokers;
    }
}
