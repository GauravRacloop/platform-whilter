package com.whilter.pubsub;


import com.whilter.conf.internal.AbstractConfiguration;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public abstract class PubsubConfiguration extends AbstractConfiguration {

    private int sessionTimeoutMs;
    private int requestTimeoutMs;
    private int heartbeatIntervalMs;
    private int maxPollRecords;
    private int pollingFrequencyInMs = -1;
    private int retries;
    private String clientId;

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public void setRequestTimeoutMs(int requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public int getHeartbeatIntervalMs() {
        return heartbeatIntervalMs;
    }

    public void setHeartbeatIntervalMs(int heartbeatIntervalMs) {
        this.heartbeatIntervalMs = heartbeatIntervalMs;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public int getPollingFrequencyInMs() {
        return pollingFrequencyInMs;
    }

    public void setPollingFrequencyInMs(int pollingFrequencyInMs) {
        this.pollingFrequencyInMs = pollingFrequencyInMs;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
