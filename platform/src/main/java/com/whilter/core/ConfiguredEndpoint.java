package com.whilter.core;

/**
 * Created by deepakchauhan on 20/07/17.
 */
public class ConfiguredEndpoint implements Endpoint {

    private String id;

    public ConfiguredEndpoint(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
