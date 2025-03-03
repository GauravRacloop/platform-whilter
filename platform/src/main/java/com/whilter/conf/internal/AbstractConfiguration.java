package com.whilter.conf.internal;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.whilter.conf.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public abstract class AbstractConfiguration implements Configuration {

    @JsonProperty("id")
    protected String ID;

    @JsonProperty("alias")
    protected String[] aliases;

    @JsonIgnore
    private Map<String, Object> properties = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @JsonAnySetter
    void addProperty(String name, Object value) {
        this.properties.put(name, value);
    }

    @Override
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

}
