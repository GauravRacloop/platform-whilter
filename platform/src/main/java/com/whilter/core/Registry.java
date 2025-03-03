package com.whilter.core;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public interface Registry {

    void register(Service service);

    void unregister(Service service);
}
