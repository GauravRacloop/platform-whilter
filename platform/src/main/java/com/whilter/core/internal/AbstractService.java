package com.whilter.core.internal;



import com.whilter.core.Service;

import java.io.IOException;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public abstract class AbstractService implements Service {

    private static final int FAILED = 0, STOPPED = 1, STOPPING = 2, RUNNABLE = 3, STARTING = 4, STARTED = 5;

    private volatile int state = RUNNABLE;

    @Override
    public final synchronized void start() throws Exception {
        if (state >= STARTING) return;
        state = STARTING;
        try {
            this.doStart();
        } catch (Exception e) {
            state = FAILED;
            throw e;
        }
        state = STARTED;
    }

    @Override
    public final synchronized void shutdown() {
        if (state <= STOPPING) return;
        state = STOPPING;
        try {
            this.doShutdown();
        } catch (Exception e) {
            state = FAILED;
            throw e;
        }
        state = STOPPED;
    }

    protected abstract void doStart() throws Exception;

    protected abstract void doShutdown();

    @Override
    public boolean autoStart() {
        return false;
    }
}
