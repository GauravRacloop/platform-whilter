package com.minda.iconnect.spark.config;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Map;

/**
 * Created by mayank on 04/09/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LivyBatchRequest {

    private String file;
    private String proxyUser;
    private String className;
    private Collection<String> args;
    private Collection<String> jars;
    private Collection<String> pyFiles;
    private Collection<String> files;
    private String driverMemory;
    private int driverCores;
    private String executorMemory;
    private int executorCores;
    private int numExecutors;
    private Collection<String> archives;
    private String queue;
    private String name;
    private Map<String, String> conf;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Collection<String> getArgs() {
        return args;
    }

    public void setArgs(Collection<String> args) {
        this.args = args;
    }

    public Collection<String> getJars() {
        return jars;
    }

    public void setJars(Collection<String> jars) {
        this.jars = jars;
    }

    public Collection<String> getPyFiles() {
        return pyFiles;
    }

    public void setPyFiles(Collection<String> pyFiles) {
        this.pyFiles = pyFiles;
    }

    public Collection<String> getFiles() {
        return files;
    }

    public void setFiles(Collection<String> files) {
        this.files = files;
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

    public Collection<String> getArchives() {
        return archives;
    }

    public void setArchives(Collection<String> archives) {
        this.archives = archives;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getConf() {
        return conf;
    }

    public void setConf(Map<String, String> conf) {
        this.conf = conf;
    }
}
