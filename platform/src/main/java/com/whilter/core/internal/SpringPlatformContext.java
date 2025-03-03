package com.whilter.core.internal;

import com.whilter.core.PlatformContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by deepakchauhan on 10/11/17.
 */
public class SpringPlatformContext implements PlatformContext, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public <T> T get(Class<T> type) {
        return context.getBean(type);
    }

    @Override
    public <T> T get(String name, Class<T> type) {
        return context.getBean(name, type);
    }

    public ApplicationContext getContext() {
        return context;
    }
}
