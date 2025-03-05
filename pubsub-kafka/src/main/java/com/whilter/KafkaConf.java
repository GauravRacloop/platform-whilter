package com.whilter;

//import com.minda.iconnect.platform.conf.ConfArray;
//import com.minda.iconnect.platform.pubsub.PubsubConfiguration;

import com.whilter.conf.ConfArray;
import com.whilter.pubsub.PubsubConfiguration;


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
