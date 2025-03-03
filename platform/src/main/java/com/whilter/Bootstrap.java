package com.whilter;

import com.whilter.core.PlatformContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Bootstrap {

    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    private static final int FAILED = 0, STOPPED = 0, STARTING = 1, STARTED = 2, STOPPING = 3;

    private volatile int state = STOPPED;
    private Object joinLock = new Object();
    private Class<?>[] springConfigs;
    private AnnotationConfigApplicationContext ctx;
    private PlatformContext platformContext;

    public Bootstrap(boolean addShutDownHook, Class<?>... springConfigs) {
        try {
            this.springConfigs = springConfigs;

            if (addShutDownHook) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    this.close();
                }));
            }

        } catch (Exception e) {
            state = FAILED;
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public ApplicationContext getCtx() {
        return ctx;
    }

    public PlatformContext getPlatformContext() {
        return platformContext;
    }

    public void load() throws Exception {
        load(true);
    }

    public void load(boolean join) throws Exception {
        log.info("Starting Application...");
        state = STARTING;

        try {
            ctx = new AnnotationConfigApplicationContext(springConfigs);
            platformContext = ctx.getBean(PlatformContext.class);
            state = STARTED;
            log.info("Application Started...");
            if (join) {
                join();
            }
        } catch (Exception e) {
            log.error("Failed to start the Application...", e);
            state = FAILED;
            this.close();
            throw e;
        }
    }

    public void close() {
        log.info("Stopping Application...");
        state = STOPPING;
        try {
            ctx.close();
        } catch (Exception e) {
            state = FAILED;
            throw e;
        } finally {
            try {
                stop();
            } catch (Exception e) {
                //Eat me
            }
        }
        state = STOPPED;
        log.info("Application Stopped...");
    }

    private void join() throws InterruptedException {
        synchronized (joinLock) {
            while (isRunning())
                joinLock.wait();
        }

        while (isStopping())
            Thread.sleep(1);
    }

    private boolean isRunning() {
        final int state = this.state;
        return state == STARTED || state == STARTING;
    }

    private boolean isStopping() {
        return state == STOPPING;
    }

    private void stop() throws InterruptedException {
        synchronized (joinLock) {
            joinLock.notifyAll();
        }
    }
}
