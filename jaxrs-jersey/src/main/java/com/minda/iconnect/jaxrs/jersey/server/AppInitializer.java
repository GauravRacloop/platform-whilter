package com.minda.iconnect.jaxrs.jersey.server;

import com.minda.iconnect.platform.AppStarter;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class AppInitializer implements ServletContainerInitializer {

    public static ServletContext ctx;
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {
        AppInitializer.ctx = ctx;
        try {
            AppStarter.start(new String[]{}, false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
