package com.whilter.core.internal;

import com.whilter.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by deepakchauhan on 08/07/17.
 */
public abstract class AbstractComponent<E extends Endpoint, S extends Service> implements Component<E, S>, ContextAware, Registry {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractComponent.class);

    private static final int FAILED = 0, STOPPED = 1, STOPPING = 2, RUNNABLE = 3, STARTING = 4, STARTED = 5;

    private volatile int state = RUNNABLE;

    private PlatformContext context;

    private Collection<Service> services = new ArrayList<>();

    private Map<String, Service> serviceMap = new ConcurrentHashMap<>();

    @Override
    public PlatformContext getContext() {
        return context;
    }

    public void setContext(PlatformContext context) {
        this.context = context;
    }

    @Override
    public final synchronized void init() throws IOException {
        if (state >= STARTING) return;
        state = STARTING;
        try {
            this.doInit();
        } catch (Exception e) {
            state = FAILED;
            throw e;
        }
        state = STARTED;
    }

    @Override
    public final synchronized void destroy() {
        if (state <= STOPPING) return;
        state = STOPPING;
        try {
            doDestroy();
            for (Service service : services) {
                service.shutdown();
            }
        } catch (Exception e) {
            state = FAILED;
            throw e;
        }
        state = STOPPED;
    }

    protected void doInit() throws IOException {
        //Override to implement the behaviour
    }

    protected void doDestroy() {
        //Override to implement the behaviour
    }

    @Override
    public final void register(Service service) {
        services.add(service);
    }

    @Override
    public void unregister(Service service) {
        services.remove(service);
    }

    @Override
    public S get(E endpoint) {
        S service = null;
        if (cache()) {
            service = (S) serviceMap.get(toUniqueID(endpoint));
        }

        if (service == null) {
            synchronized (this) {
                if (cache()) {
                    service = (S) serviceMap.get(toUniqueID(endpoint));
                }
                if (service == null) {
                    service = doGet(endpoint);
                    if (cache()) {
                        serviceMap.put(toUniqueID(endpoint), service);
                    }
                    this.register(service);
                    if (service.autoStart()) {
                        try {
                            service.start();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return service;
    }

    protected abstract S doGet(E endpoint);

    protected String toUniqueID(E endpoint) {
        return endpoint.toString();
    }

    protected boolean cache() {
        return false;
    }
}
