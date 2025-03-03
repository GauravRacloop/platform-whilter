package com.whilter.core;

import java.io.IOException;

/**
 * Created by deepakchauhan on 15/07/17.
 */
public interface Service {

    void start() throws Exception;

    void shutdown();

    boolean autoStart();
}
