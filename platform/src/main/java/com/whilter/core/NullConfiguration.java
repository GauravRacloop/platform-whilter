package com.whilter.core;

import com.whilter.conf.Configuration;

/**
 * Created by deepakchauhan on 08/07/17.
 */
public class NullConfiguration implements Configuration {

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
