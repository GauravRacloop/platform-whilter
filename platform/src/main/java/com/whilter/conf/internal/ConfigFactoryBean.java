package com.whilter.conf.internal;

import com.whilter.conf.Configuration;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by deepakchauhan on 14/07/17.
 */
public class ConfigFactoryBean<T extends Configuration> implements FactoryBean<T> {

    private T conf;

    public ConfigFactoryBean(T conf) {
        this.conf = conf;
    }

    @Override
    public T getObject() throws Exception {
        return conf;
    }

    @Override
    public Class<?> getObjectType() {
        return conf.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
