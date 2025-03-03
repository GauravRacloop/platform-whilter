package com.minda.iconnect.livy.job;

import com.minda.iconnect.platform.Bootstrap;
import com.minda.iconnect.platform.core.PlatformContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deepakc on 03/01/17.
 */
public final class JobContextFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobContextFactory.class);

    private static Map<String, PlatformContext> contextMap = new HashMap<>();

    private static Bootstrap bootstrap;

    public static PlatformContext getPlatformContext(Class<?> springConfig) {
        if (springConfig == null) return null;

        PlatformContext platformContext = contextMap.get(springConfig.getName());
        if (platformContext == null) {
            synchronized (JobContextFactory.class) {
                if (platformContext == null) {
                    try {
                        LOGGER.warn("Platform Context found null. Initializing....");
                        bootstrap = new Bootstrap(true, springConfig);
                        bootstrap.load(false);
                        platformContext = bootstrap.getPlatformContext();
                        contextMap.put(springConfig.getName(), platformContext);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return platformContext;
    }

    public static void closeContext() {
        bootstrap.close();
    }
}
