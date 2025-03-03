package com.minda.iconnect.spark.config;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

/**
 * Created by mayank on 28/08/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LivySessionRequest {
    private String name;
    private String kind;
    private String driverMemory;
    private int driverCores;
    private String executorMemory;
    private int executorCores;
    private int numExecutors;
    private String queue;
    private int heartbeatTimeoutInSecond;
    private Collection<String> jars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public int getDriverCores() {
        return driverCores;
    }

    public void setDriverCores(int driverCores) {
        this.driverCores = driverCores;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public int getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(int executorCores) {
        this.executorCores = executorCores;
    }

    public int getNumExecutors() {
        return numExecutors;
    }

    public void setNumExecutors(int numExecutors) {
        this.numExecutors = numExecutors;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public int getHeartbeatTimeoutInSecond() {
        return heartbeatTimeoutInSecond;
    }

    public void setHeartbeatTimeoutInSecond(int heartbeatTimeoutInSecond) {
        this.heartbeatTimeoutInSecond = heartbeatTimeoutInSecond;
    }

    public Collection<String> getJars() {
        return jars;
    }

    public void setJars(Collection<String> jars) {
        this.jars = jars;
    }
}
