package com.whilter.conf;

import com.whilter.conf.internal.AbstractConfiguration;

/**
 * Created by mayank on 30/10/17.
 */
public class GISConf extends AbstractConfiguration {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
