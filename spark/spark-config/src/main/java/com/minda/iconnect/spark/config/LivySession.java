package com.minda.iconnect.spark.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minda.iconnect.platform.conf.internal.AbstractConfiguration;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by mayank on 28/08/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LivySession {

    private int id;
    private String appName;
    private String appId;
    private String owner;
    private String proxyUser;
    private SparkApplication.Kind kind;
    private Collection<String> log;
    private String state;
    private HashMap<String, String> appInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public SparkApplication.Kind getKind() {
        return kind;
    }

    public void setKind(SparkApplication.Kind kind) {
        this.kind = kind;
    }

    public Collection<String> getLog() {
        return log;
    }

    public void setLog(Collection<String> log) {
        this.log = log;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public HashMap<String, String> getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(HashMap<String, String> appInfo) {
        this.appInfo = appInfo;
    }
}
