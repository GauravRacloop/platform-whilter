package com.whilter.conf;

import com.whilter.conf.internal.DefaultConfigReader;

import java.util.Collection;

/**
 * Created by deepakchauhan on 13/07/17.
 */
public interface ConfigReader {

    static ConfigReader get() {
        return DefaultConfigReader.getInstance();
    }

    <T extends Configuration> T read(String id, Class<T> type);

    <T extends Configuration> Collection<T> read(Class<T> type);

    <T extends Configuration> T readSingleAny(Class<T> type);

}
