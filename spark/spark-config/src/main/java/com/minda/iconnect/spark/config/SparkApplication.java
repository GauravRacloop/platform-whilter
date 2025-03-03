package com.minda.iconnect.spark.config;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.conf.internal.AbstractConfiguration;

import java.util.Collection;

/**
 * Created by deepakc on 02/12/16.
 */
@JsonRootName("sparkApplication")
@ConfArray(SparkApplication[].class)
public class SparkApplication extends AbstractConfiguration {

    private String name;
    private Kind kind;
    private String driverMemory;
    private int driverCores;
    private String executorMemory;
    private int executorCores;
    private int numExecutors;
    private String queue;
    private int heartbeatTimeoutInSecond;
    private Collection<String> jobs;
    private JobType jobType;

    public String getName() {
        return name == null? ID : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
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

    public Collection<String> getJobs() {
        return jobs;
    }

    public void setJobs(Collection<String> jobs) {
        this.jobs = jobs;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public enum Kind {
        spark, pyspark, pyspark3, sparkr
    }

    public enum JobType {
        BATCH, INTERACTIVE
    }

}
