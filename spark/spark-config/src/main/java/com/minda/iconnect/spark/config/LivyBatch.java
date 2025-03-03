package com.minda.iconnect.spark.config;

import java.util.Collection;
import java.util.Map;

/**
 * Created by mayank on 04/09/17.
 */
public class LivyBatch {

    private int id;
    private String appId;
    private Map<String, String> appInfo;
    private Collection<String> log;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Map<String, String> getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(Map<String, String> appInfo) {
        this.appInfo = appInfo;
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
}
