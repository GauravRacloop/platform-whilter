package com.minda.iconnect.saga;

import java.io.Serializable;
import java.util.Map;

public class Change implements Serializable {

    private static final long serialVersionUID = 2L;

    private String resourceType;
    private long resourceId;
    private Operation operation;

    private Map<String, String> data;

    public Change(String resourceType, long resourceId, Operation operation) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.operation = operation;
    }

    public String getResourceType() {
        return resourceType;
    }

    public long getResourceId() {
        return resourceId;
    }

    public Operation getOperation() {
        return operation;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public enum Operation {
        CREATED, MODIFIED, DELETED
    }
}
