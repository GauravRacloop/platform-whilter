package com.whilter.conf;

import com.whilter.conf.internal.AbstractConfiguration;

/**
 * Created by deepakchauhan on 21/07/17.
 */
public class GlobalConf extends AbstractConfiguration {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
